package com.jdevs.timeo.data.records

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID
import com.jdevs.timeo.util.FirestoreConstants.NAME
import com.jdevs.timeo.util.FirestoreConstants.TIMESTAMP
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.RecordsConstants.FIRESTORE_RECORDS_PAGE_SIZE
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsRemoteDataSource : RecordsDataSource {

    override val records: ItemsLiveData?

    override suspend fun addRecord(record: Record): WriteBatch

    override suspend fun deleteRecord(record: Record): WriteBatch

    suspend fun renameRecords(activityId: String, newName: String, batch: WriteBatch)

    fun resetMonitor()
}

@Singleton
class FirestoreRecordsDataSource @Inject constructor(
    authRepository: AuthRepository,
    private val mapper: FirestoreRecordMapper,
    domainMapper: FirestoreDomainRecordMapper
) :
    FirestoreListDataSource(authRepository),
    RecordsRemoteDataSource {

    private val recordsMonitor =
        createCollectionMonitor(
            FirestoreRecord::class, domainMapper,
            FIRESTORE_RECORDS_PAGE_SIZE, TIMESTAMP
        )

    override val records
        get() = recordsMonitor.safeAccess().getLiveData()

    private var recordsRef: CollectionReference by SafeAccess()

    override suspend fun addRecord(record: Record): WriteBatch {

        val newRecordRef = recordsRef.document()
        return db.batch().also { it.set(newRecordRef, mapper.map(record)) }
    }

    override suspend fun deleteRecord(record: Record): WriteBatch {

        val recordRef = recordsRef.document(record.documentId)
        return db.batch().also { it.delete(recordRef) }
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

    override fun resetMonitor() = recordsMonitor.reset()
}
