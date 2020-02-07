package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.FieldValue.increment
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.ACTIVITIES_COLLECTION
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.RECENT_RECORDS
import com.jdevs.timeo.data.RECORDS_COLLECTION
import com.jdevs.timeo.data.TIME
import com.jdevs.timeo.data.TIMESTAMP
import com.jdevs.timeo.data.TOTAL_TIME
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.data.stats.FirestoreDayStats
import com.jdevs.timeo.data.util.getDaysSinceEpoch
import com.jdevs.timeo.data.util.getMonthSinceEpoch
import com.jdevs.timeo.data.util.getWeeksSinceEpoch
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsRemoteDataSource : RecordsDataSource {

    val records: List<LiveData<Operation<Record>>>

    override suspend fun addRecord(record: Record)
}

@Singleton
class FirestoreRecordsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    RecordsRemoteDataSource {

    private val recordsWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreRecord::mapToDomain, TIMESTAMP)

    override val records
        get() = recordsWatcher.safeAccess().getLiveDataList()

    private var recordsRef: CollectionReference by SafeAccess()
    private var activitiesRef: CollectionReference by SafeAccess()
    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override suspend fun addRecord(record: Record) = withContext<Unit>(Dispatchers.IO) {

        val newRecordRef = recordsRef.document()

        db.runBatch { batch ->

            batch.set(newRecordRef, record.mapToFirestore())
            increaseActivityTime(record.activityId, record.time, batch)
            launch { updateStats(record.creationDate, record.time) }
        }
    }

    override suspend fun deleteRecord(record: Record) = withContext<Unit>(Dispatchers.IO) {

        db.runBatch { batch ->

            val recordRef = recordsRef.document(record.documentId)
            batch.delete(recordRef)

            increaseActivityTime(record.activityId, -record.time, batch)
            launch { updateStats(record.creationDate, -record.time) }
        }
    }

    private fun increaseActivityTime(activityId: String, time: Long, batch: WriteBatch) {

        val activityRef = activitiesRef.document(activityId)
        val record = RecordMinimal(time.toInt())

        batch.update(activityRef, TOTAL_TIME, increment(time))

        if (time > 0) {

            batch.update(activityRef, RECENT_RECORDS, arrayUnion(record))
        }
    }

    private suspend fun updateStats(creationDate: OffsetDateTime, time: Long) {

        val refs = listOf(
            dayStatsRef.document(creationDate.getDaysSinceEpoch().toString()),
            weekStatsRef.document(creationDate.getWeeksSinceEpoch().toString()),
            monthStatsRef.document(creationDate.getMonthSinceEpoch().toString())
        )

        refs.forEach { ref ->

            try {

                ref.update(TIME, increment(time)).await()
            } catch (e: FirebaseFirestoreException) {

                if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                    ref.set(FirestoreDayStats(time = time, day = ref.id.toLong()))
                }
            }
        }
    }

    override fun resetRefs(uid: String) {

        recordsRef = createRef(uid, RECORDS_COLLECTION, recordsWatcher)
        activitiesRef = createRef(uid, ACTIVITIES_COLLECTION)
        dayStatsRef = createRef(uid, DAY_STATS_COLLECTION)
        weekStatsRef = createRef(uid, WEEK_STATS_COLLECTION)
        monthStatsRef = createRef(uid, MONTH_STATS_COLLECTION)
    }

    companion object {
        private const val PAGE_SIZE = 50L
    }
}
