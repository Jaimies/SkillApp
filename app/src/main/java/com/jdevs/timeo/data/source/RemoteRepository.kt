package com.jdevs.timeo.data.source

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.ItemsLiveData
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.logOnFailure

@Suppress("StaticFieldLeak")
object RemoteRepository {

    val activitiesLiveData: ItemsLiveData? get() = activitiesDataSource.getLiveData()
    val recordsLiveData: ItemsLiveData? get() = recordsDataSource.getLiveData()

    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference
    private lateinit var activitiesDataSource: RemoteDataSource
    private lateinit var recordsDataSource: RemoteDataSource

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    fun initialize(activitiesDataSource: RemoteDataSource, recordsDataSource: RemoteDataSource) {

        this.recordsDataSource = recordsDataSource
        this.activitiesDataSource = activitiesDataSource

        initializeRefs()
        activitiesDataSource.setup(activitiesRef)
        recordsDataSource.setup(recordsRef)
    }

    fun setupActivitiesSource(onLastItemCallback: () -> Unit) {

        activitiesDataSource.onLastItemCallback = onLastItemCallback
        initializeRefs()
        activitiesDataSource.setup(activitiesRef)
    }

    fun setupRecordsSource(onLastItemCallback: () -> Unit) {

        recordsDataSource.onLastItemCallback = onLastItemCallback
        initializeRefs()
        recordsDataSource.setup(recordsRef)
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

    suspend fun saveActivity(activity: Activity, activityId: String) {

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

    fun addActivity(activity: Activity) {

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

    private fun initializeRefs() {

        val uid = auth.currentUser?.uid ?: "null"

        activitiesRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${ActivitiesConstants.COLLECTION}")

        recordsRef =
            firestore.collection("/$USERS_COLLECTION/$uid/${RecordsConstants.COLLECTION}")
    }

    private const val USERS_COLLECTION = "users"
}
