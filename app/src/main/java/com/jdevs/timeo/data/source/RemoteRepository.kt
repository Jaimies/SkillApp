package com.jdevs.timeo.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.livedata.ItemListLiveData
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.UserConstants
import com.jdevs.timeo.util.logOnFailure

object RemoteRepository : FirebaseAuth.AuthStateListener {

    val activitiesLiveData: ItemListLiveData?
        get() {

            if (!isUserAuthenticated) {

                return activitiesDataSource.getAwaitingLiveData()
            }

            return activitiesDataSource.getLiveData()
        }

    val recordsLiveData: ItemListLiveData?
        get() {

            if (!isUserAuthenticated) {

                return activitiesDataSource.getAwaitingLiveData()
            }

            return recordsDataSource.getLiveData()
        }

    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference
    private lateinit var activitiesDataSource: RemoteDataSource
    private lateinit var recordsDataSource: RemoteDataSource

    private var prevUid: String? = null
    private var isUserAuthenticated = true
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    fun setup(
        activitiesDataSource: RemoteDataSource,
        recordsDataSource: RemoteDataSource
    ) {

        val uid = auth.currentUser?.uid

        if (uid == null) {

            isUserAuthenticated = false
            auth.addAuthStateListener(this)
            return
        }

        initializeRefs(uid)

        this.activitiesDataSource =
            activitiesDataSource(ref = activitiesRef) as ActivitiesDataSource
        this.recordsDataSource = recordsDataSource(ref = recordsRef) as RecordsDataSource
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {

        val uid = auth.currentUser?.uid

        if (uid != null && uid != prevUid) {

            initializeRefs(uid)
            prevUid = uid

            activitiesDataSource.onUserAuthenticated(activitiesRef)
            recordsDataSource.onUserAuthenticated(recordsRef)

            auth.removeAuthStateListener(this)
        }
    }

    fun setOnLastActivityCallback(callback: () -> Unit) {

        activitiesDataSource.onLastItemCallback = callback
    }

    fun setOnLastRecordCallback(callback: () -> Unit) {

        recordsDataSource.onLastItemCallback = callback
    }

    fun createRecord(activityName: String, time: Long, activityId: String) {

        val record = Record(activityName, time, activityId)

        recordsRef.add(record)

        activitiesRef
            .document(activityId)
            .update(ActivitiesConstants.TOTAL_TIME_PROPERTY, FieldValue.increment(time))
            .logOnFailure("Failed to update data in Firestore")
    }

    fun updateActivity(activity: TimeoActivity, activityId: String) {

        val activityReference = activitiesRef.document(activityId)

        recordsRef.whereEqualTo(ActivitiesConstants.ACTIVITY_ID_PROPERTY, activityId).get()
            .addOnSuccessListener { querySnapshot ->

                val recordReferences = mutableListOf<DocumentReference>()

                for (record in querySnapshot.documents) {

                    recordReferences.add(record.reference)
                }

                firestore.runBatch { batch ->

                    batch.set(activityReference, activity)

                    for (recordReference in recordReferences) {

                        batch.update(
                            recordReference,
                            ActivitiesConstants.NAME_PROPERTY,
                            activity.name
                        )
                    }
                }
                    .logOnFailure("Failed to save data to Firestore")
            }
    }

    fun createActivity(activity: TimeoActivity) {

        activitiesRef.add(activity)
            .logOnFailure("Failed to add data to Firestore")
    }

    fun deleteActivity(activityId: String) {

        activitiesRef.document(activityId).delete()
            .logOnFailure("Failed to delete data to Firestore")
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(ActivitiesConstants.TOTAL_TIME_PROPERTY, FieldValue.increment(-recordTime))
    }

    private fun initializeRefs(uid: String) {

        activitiesRef =
            firestore.collection("/${UserConstants.USERS_COLLECTION}/$uid/${ActivitiesConstants.COLLECTION}")

        recordsRef =
            firestore.collection("/${UserConstants.USERS_COLLECTION}/$uid/${RecordsConstants.COLLECTION}")
    }
}
