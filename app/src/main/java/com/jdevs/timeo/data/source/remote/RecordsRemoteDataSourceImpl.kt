package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await

class RecordsRemoteDataSourceImpl(
    private val recordsMonitor: CollectionMonitor,
    authRepository: AuthRepository
) : BaseRemoteDataSource(authRepository), RecordsRemoteDataSource {

    override val records
        get() = recordsMonitor.getLiveData().also { reset() }

    private lateinit var recordsRef: CollectionReference

    override suspend fun addRecord(record: Record): WriteBatch {

        record.setupFirestoreTimestamp()

        val newRecordRef = recordsRef.document()

        return firestore.batch().also { batch ->

            batch.set(newRecordRef, record)
        }
    }

    override suspend fun deleteRecord(record: Record): WriteBatch {

        record.setupFirestoreTimestamp()

        val recordRef = recordsRef.document(record.documentId)

        return firestore.batch().also { batch ->

            batch.delete(recordRef)
        }
    }

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) {

        val querySnapshot = recordsRef
            .whereEqualTo(FirestoreConstants.ACTIVITY_ID_PROPERTY, activityId)
            .get().await()

        for (document in querySnapshot.documents) {

            batch.update(document.reference, NAME_PROPERTY, newName)
        }

        batch.commit()
    }

    override fun resetRefs(uid: String) {

        recordsRef = createRef(uid, RecordsConstants.COLLECTION, recordsMonitor)
    }

    override fun resetRecordsMonitor() = recordsMonitor.reset()
}
