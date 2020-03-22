package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue.increment
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.toObject
import com.jdevs.timeo.data.ACTIVITIES_COLLECTION
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.RECENT_RECORDS
import com.jdevs.timeo.data.RECORDS_COLLECTION
import com.jdevs.timeo.data.TIME
import com.jdevs.timeo.data.TIMESTAMP
import com.jdevs.timeo.data.TOTAL_TIME
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.activities.FirestoreActivity
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.shared.util.WEEK_DAYS
import com.jdevs.timeo.shared.util.daysSinceEpoch
import com.jdevs.timeo.shared.util.monthSinceEpoch
import com.jdevs.timeo.shared.util.weeksSinceEpoch
import kotlinx.coroutines.tasks.await
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import javax.inject.Singleton

interface RecordsRemoteDataSource : RecordsDataSource {

    fun getRecords(fetchNewItems: Boolean): List<LiveData<Operation<Record>>>
}

@Singleton
class FirestoreRecordsDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    RecordsRemoteDataSource {

    private val recordsWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreRecord::mapToDomain, TIMESTAMP)

    override fun getRecords(fetchNewItems: Boolean) =
        recordsWatcher.safeAccess().getLiveDataList(fetchNewItems)

    private var recordsRef: CollectionReference by SafeAccess()
    private var activitiesRef: CollectionReference by SafeAccess()
    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override suspend fun addRecord(record: Record) {

        val newRecordRef = recordsRef.document()

        db.runBatch { batch ->

            batch.updateStats(record.creationDate, record.time)
            batch.set(newRecordRef, record.mapToFirestore())
        }

        increaseActivityTime(record.activityId, record.time)
    }

    override suspend fun deleteRecord(record: Record) {

        db.runBatch { batch ->

            batch.updateStats(record.creationDate, -record.time)
            batch.delete(recordsRef.document(record.id))
        }

        increaseActivityTime(record.activityId, -record.time)
    }

    private suspend fun increaseActivityTime(activityId: String, time: Int) {

        val activityRef = activitiesRef.document(activityId)
        val activity = activityRef.get(Source.CACHE).await().toObject<FirestoreActivity>()!!

        val newRecords = activity.recentRecords.toMutableList()

        newRecords.removeAll {

            it.creationDate.toLocalDate()
                .isBefore(OffsetDateTime.now().minusDays((WEEK_DAYS - 1L)).toLocalDate())
        }

        val index = newRecords.indexOfFirst { it.day == OffsetDateTime.now().daysSinceEpoch }

        when {
            index != -1 -> newRecords[index] = RecordMinimal(newRecords[index].time + time)
            time > 0 -> newRecords.add(RecordMinimal(time))
            else -> return
        }

        activityRef.update(TOTAL_TIME, increment(time.toLong()), RECENT_RECORDS, newRecords)
    }

    private fun WriteBatch.updateStats(creationDate: OffsetDateTime, time: Int) {

        fun DocumentReference.updateStats() {
            set(
                this, hashMapOf(TIME to increment(time.toLong()), DAY to id.toInt()),
                SetOptions.merge()
            )
        }

        dayStatsRef.document(creationDate.daysSinceEpoch.toString()).updateStats()
        weekStatsRef.document(creationDate.weeksSinceEpoch.toString()).updateStats()
        monthStatsRef.document(creationDate.monthSinceEpoch.toString()).updateStats()
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
