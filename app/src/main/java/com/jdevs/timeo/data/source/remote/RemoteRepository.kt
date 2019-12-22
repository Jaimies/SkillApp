package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.logOnFailure

object RemoteRepository {

    val activitiesLiveData: ItemsLiveData? get() = activitiesDataSource.getLiveData()
    val recordsLiveData: ItemsLiveData? get() = recordsDataSource.getLiveData()

    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference
    private lateinit var activitiesDataSource: RemoteDataSource
    private lateinit var recordsDataSource: RemoteDataSource

    private val firestore = FirebaseFirestore.getInstance()

    fun initialize(activitiesDataSource: RemoteDataSource, recordsDataSource: RemoteDataSource) {

        RemoteRepository.recordsDataSource = recordsDataSource
        RemoteRepository.activitiesDataSource = activitiesDataSource

        initializeActivitiesRef()
        initializeRecordsRef()

        activitiesDataSource.setup(activitiesRef)
        recordsDataSource.setup(recordsRef)
    }

    fun setupActivitiesSource(onLastItemCallback: () -> Unit) {

        activitiesDataSource.onLastItemCallback = onLastItemCallback
        initializeActivitiesRef()
        activitiesDataSource.setup(
            activitiesRef
        )
    }

    fun setupRecordsSource(onLastItemCallback: () -> Unit) {

        recordsDataSource.onLastItemCallback = onLastItemCallback
        initializeRecordsRef()
        recordsDataSource.setup(
            recordsRef
        )
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

    private fun initializeActivitiesRef() {

        activitiesRef =
            firestore.collection("/$USERS_COLLECTION/${AuthRepository.uid}/${ActivitiesConstants.COLLECTION}")
    }

    private fun initializeRecordsRef() {

        recordsRef =
            firestore.collection("/$USERS_COLLECTION/${AuthRepository.uid}/${RecordsConstants.COLLECTION}")
    }

    private const val USERS_COLLECTION = "users"
}
