package com.jdevs.timeo.data.records

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID
import com.jdevs.timeo.util.FirestoreConstants.NAME
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.await
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsRemoteDataSource : RecordsDataSource {

    override val records: ItemsLiveData?

    override suspend fun addRecord(record: Record): WriteBatch

    override suspend fun deleteRecord(record: Record): WriteBatch

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetRecordsMonitor()
}

@Singleton
class RecordsRemoteDataSourceImpl @Inject constructor(
    authRepository: AuthRepository
) : FirestoreDataSource(authRepository), RecordsRemoteDataSource {

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
            .whereEqualTo(ACTIVITY_ID, activityId)
            .get().await()

        for (document in querySnapshot.documents) {

            batch.update(document.reference, NAME, newName)
        }

        batch.commit()
    }

    override fun resetRefs(uid: String) {

        recordsRef = createRef(uid, RecordsConstants.COLLECTION, recordsMonitor)
    }

    override fun resetRecordsMonitor() = recordsMonitor.reset()
}
