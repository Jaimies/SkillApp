package com.jdevs.timeo.data.source

import await
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.livedata.ItemsLiveData
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.UserConstants
import com.jdevs.timeo.util.logOnFailure

@Suppress("TooManyFunctions", "StaticFieldLeak")
object RemoteRepository : FirebaseAuth.AuthStateListener {

    val activitiesLiveData: ItemsLiveData?
        get() {

            if (!isUserAuthenticated) {

                return activitiesDataSource.getAwaitingLiveData()
            }

            return activitiesDataSource.getLiveData()
        }

    val recordsLiveData: ItemsLiveData?
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

    private var isUserAuthenticated = true
    private var prevUid: String? = null
    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun initialize(activitiesDataSource: RemoteDataSource, recordsDataSource: RemoteDataSource) {

        this.recordsDataSource = recordsDataSource
        this.activitiesDataSource = activitiesDataSource

        initializeRefs {

            activitiesDataSource.setup(activitiesRef)
            recordsDataSource.setup(recordsRef)
        }
    }

    override fun onAuthStateChanged(auth: FirebaseAuth) {

        val uid = auth.currentUser?.uid

        if (uid != null && uid != prevUid) {

            setupRefs(uid)
            prevUid = uid

            isUserAuthenticated = true

            activitiesDataSource.onUserAuthenticated(activitiesRef)
            recordsDataSource.onUserAuthenticated(recordsRef)

            auth.removeAuthStateListener(this)
        }
    }

    fun setupActivitiesSource(onLastItemCallback: () -> Unit) {

        activitiesDataSource.onLastItemCallback = onLastItemCallback

        initializeRefs {

            activitiesDataSource.setup(activitiesRef)
        }
    }

    fun setupRecordsSource(onLastItemCallback: () -> Unit) {

        recordsDataSource.onLastItemCallback = onLastItemCallback

        initializeRefs {

            recordsDataSource.setup(recordsRef)
        }
    }

    fun addRecord(activityName: String, time: Long, activityId: String) {

        val record = Record(activityName, time, activityId)

        val newRecordRef = recordsRef.document()
        val activityRef = activitiesRef.document(activityId)

        firestore.runBatch { batch ->

            batch.set(newRecordRef, record)
            batch.update(activityRef, TOTAL_TIME_PROPERTY, FieldValue.increment(time))
        }
    }

    suspend fun saveActivity(activity: TimeoActivity, activityId: String) {

        val activityRef = activitiesRef.document(activityId)

        val querySnapshot = recordsRef
            .whereEqualTo(ACTIVITY_ID_PROPERTY, activityId)
            .get().await()

        firestore.runBatch { batch ->

            batch.set(activityRef, activity)

            for (document in querySnapshot.documents) {

                batch.update(document.reference, NAME_PROPERTY, activity.name)
            }
        }
            .logOnFailure("Failed to save data to Firestore")
    }

    fun addActivity(activity: TimeoActivity) {

        activitiesRef.add(activity)
            .logOnFailure("Failed to add data to Firestore")
    }

    fun deleteActivity(activityId: String) {

        activitiesRef.document(activityId).delete()
            .logOnFailure("Failed to delete data to Firestore")
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        val recordRef = recordsRef.document(id)
        val activityRef = activitiesRef.document(activityId)

        firestore.runBatch { batch ->

            batch.delete(recordRef)

            batch.update(
                activityRef,
                TOTAL_TIME_PROPERTY,
                FieldValue.increment(-recordTime)
            )
        }
    }

    private fun initializeRefs(ifAuthenticated: () -> Unit = {}) {

        val uid = auth.currentUser?.uid

        if (uid == null) {

            isUserAuthenticated = false
            auth.addAuthStateListener(this)
            return
        }

        setupRefs(uid)
        ifAuthenticated()
    }

    private fun setupRefs(uid: String) {

        activitiesRef =
            firestore.collection("/${UserConstants.USERS_COLLECTION}/$uid/${ActivitiesConstants.COLLECTION}")

        recordsRef =
            firestore.collection("/${UserConstants.USERS_COLLECTION}/$uid/${RecordsConstants.COLLECTION}")
    }
}
