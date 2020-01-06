package com.jdevs.timeo.data.source.remote

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecordsRemoteDataSourceImpl @Inject constructor(
    authRepository: AuthRepository
) : BaseRemoteDataSource(authRepository), RecordsRemoteDataSource {

    private val recordsMonitor =
        createCollectionMonitor(Record::class.java, RecordsConstants.PAGE_SIZE)

    override val records
        get() = recordsMonitor.safeAccess().getLiveData()

    private var recordsRef: CollectionReference by SafeInit()

    override suspend fun addRecord(record: Record): WriteBatch {

        record.setupFirestoreTimestamp()

        val newRecordRef = recordsRef.document()

        return db.batch().also { batch ->

            batch.set(newRecordRef, record)
        }
    }

    override suspend fun deleteRecord(record: Record): WriteBatch {

        record.setupFirestoreTimestamp()

        val recordRef = recordsRef.document(record.documentId)

        return db.batch().also { batch ->

            batch.delete(recordRef)
        }
    }

    override suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch) {

        val querySnapshot = recordsRef
            .whereEqualTo(ACTIVITY_ID_PROPERTY, activityId)
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
