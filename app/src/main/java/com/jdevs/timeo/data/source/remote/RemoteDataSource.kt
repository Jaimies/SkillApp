package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.TimeoDataSource
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.logOnFailure

class RemoteDataSource(
    private val activitiesMonitor: CollectionMonitor,
    private val recordsMonitor: CollectionMonitor
) : TimeoDataSource {

    override val activitiesLiveData get() = activitiesMonitor.getLiveData()
    override val recordsLiveData get() = recordsMonitor.getLiveData()

    private val firestore = FirebaseFirestore.getInstance()
    private var prevUid = ""
    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference

    init {

        resetCollectionMonitors()
    }

    override suspend fun addRecord(record: Record) {

        val newRecordRef = recordsRef.document()
        val activityRef = activitiesRef.document(record.activityId)

        firestore.runBatch { batch ->

            batch.set(newRecordRef, record)
            batch.update(activityRef, TOTAL_TIME_PROPERTY, FieldValue.increment(record.time))
        }
    }

    override suspend fun saveActivity(activity: Activity) {

        val activityRef = activitiesRef.document(activity.documentId)

        val querySnapshot = recordsRef
            .whereEqualTo(ACTIVITY_ID_PROPERTY, activity.documentId)
            .get().await()

        firestore.runBatch { batch ->

            batch.set(activityRef, activity)

            for (document in querySnapshot.documents) {

                batch.update(document.reference, NAME_PROPERTY, activity.name)
            }
        }
            .logOnFailure("Failed to save data to Firestore")
    }

    override suspend fun addActivity(activity: Activity) {

        activitiesRef.add(activity)
            .logOnFailure("Failed to add data to Firestore")
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.documentId).delete()
            .logOnFailure("Failed to delete data to Firestore")
    }

    override suspend fun deleteRecord(record: Record) {

        val recordRef = recordsRef.document(record.documentId)
        val activityRef = activitiesRef.document(record.activityId)

        firestore.runBatch { batch ->

            batch.delete(recordRef)

            batch.update(
                activityRef,
                TOTAL_TIME_PROPERTY,
                FieldValue.increment(-record.time)
            )
        }
    }

    override fun resetRecordsMonitor() {

        recordsMonitor.reset()
    }

    override fun reset() = resetCollectionMonitors()

    private fun resetCollectionMonitors() {

        val uid = AuthRepository.uid.orEmpty()

        if (uid == prevUid) {

            activitiesMonitor.reset()
            return
        }

        if (uid.isEmpty()) {

            return
        }

        activitiesRef = firestore
            .collection("/$USERS_COLLECTION/${uid}/${ActivitiesConstants.COLLECTION}")

        recordsRef = firestore
            .collection("/$USERS_COLLECTION/${uid}/${RecordsConstants.COLLECTION}")

        activitiesMonitor.setRef(activitiesRef)
        recordsMonitor.setRef(recordsRef)

        prevUid = uid
    }

    companion object {

        private const val USERS_COLLECTION = "users"
    }
}
