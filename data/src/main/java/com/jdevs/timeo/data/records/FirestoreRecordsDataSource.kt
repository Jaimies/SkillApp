package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.Source.CACHE
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.toObject
import com.jdevs.timeo.data.ACTIVITIES_COLLECTION
import com.jdevs.timeo.data.DAY
import com.jdevs.timeo.data.DAY_STATS_COLLECTION
import com.jdevs.timeo.data.MONTH_STATS_COLLECTION
import com.jdevs.timeo.data.RECENT_RECORDS
import com.jdevs.timeo.data.RECORDS_COLLECTION
import com.jdevs.timeo.data.TIMESTAMP
import com.jdevs.timeo.data.TOTAL_TIME
import com.jdevs.timeo.data.WEEK_STATS_COLLECTION
import com.jdevs.timeo.data.activities.FirestoreActivity
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.QueryWatcher
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.util.increment
import com.jdevs.timeo.data.util.runBatchSuspend
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.model.Record
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.shared.collections.update
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

    private val recordsWatcher = QueryWatcher(PAGE_SIZE, FirestoreRecord::mapToDomain, TIMESTAMP)

    override fun getRecords(fetchNewItems: Boolean) =
        recordsWatcher.safeAccess().getLiveDataList(fetchNewItems)

    private var recordsRef: CollectionReference by SafeAccess()
    private var activitiesRef: CollectionReference by SafeAccess()
    private var dayStatsRef: CollectionReference by SafeAccess()
    private var weekStatsRef: CollectionReference by SafeAccess()
    private var monthStatsRef: CollectionReference by SafeAccess()

    override suspend fun addRecord(record: Record) {

        val newRecordRef = recordsRef.document()

        db.runBatchSuspend { batch ->

            batch.updateStats(record, false)
            batch.set(newRecordRef, record.mapToFirestore())
            batch.incrementActivityTime(record.activityId, record.time)
        }
    }

    override suspend fun deleteRecord(record: Record) {

        db.runBatchSuspend { batch ->

            batch.updateStats(record, true)
            batch.delete(recordsRef.document(record.id))
            batch.incrementActivityTime(record.activityId, -record.time)
        }
    }

    private suspend fun WriteBatch.incrementActivityTime(
        activityId: String,
        time: Int,
        subActivityId: String = ""
    ) {

        val activityRef = activitiesRef.document(activityId)
        val activity = activityRef.get(CACHE).await().toObject<FirestoreActivity>()!!

        val newRecords = activity.recentRecords.toMutableList()

        newRecords.removeAll {

            it.creationDate.toLocalDate()
                .isBefore(OffsetDateTime.now().minusDays((WEEK_DAYS - 1L)).toLocalDate())
        }

        val index = newRecords.indexOfFirst { it.day == OffsetDateTime.now().daysSinceEpoch }

        when {
            index >= 0 -> newRecords[index] = RecordMinimal(newRecords[index].time + time)
            time > 0 -> newRecords.add(RecordMinimal(time))
            else -> return
        }

        val updates = mutableMapOf(TOTAL_TIME to increment(time), RECENT_RECORDS to newRecords)

        handleSubactivities(updates, activity, time, subActivityId)
        update(activityRef, updates)
    }

    private suspend fun WriteBatch.handleSubactivities(
        updates: MutableMap<String, Any>,
        activity: FirestoreActivity,
        time: Int,
        subActivityId: String = ""
    ) {

        if (activity.parentActivity != null) {

            updates["parentActivity.totalTime"] = increment(time)
            incrementActivityTime(activity.parentActivity.id, time, activity.documentId)
        } else if (subActivityId != "") {

            val subactivities = activity.subActivities.toMutableList()
            val subActivityIndex = subactivities.indexOfFirst { it.id == subActivityId }
                .takeIf { it >= 0 } ?: return

            subactivities.update(subActivityIndex) { it.copy(totalTime = it.totalTime + time) }
            updates["subActivities"] = subactivities
        } else if (activity.subActivities.isNotEmpty()) {

            activity.subActivities.forEach {
                activitiesRef.document(it.id).update("parentActivity.totalTime", increment(time))
            }
        }
    }

    private fun WriteBatch.updateStats(record: Record, decrease: Boolean) {

        val timeIncrement = increment(if (decrease) -record.time else record.time)

        fun DocumentReference.updateStats() {
            set(
                this, mapOf(
                    TOTAL_TIME to timeIncrement, DAY to id.toInt(),
                    "activityTimes" to mapOf(record.activityId to timeIncrement)
                ), SetOptions.merge()
            )
        }

        dayStatsRef.document(record.creationDate.daysSinceEpoch.toString()).updateStats()
        weekStatsRef.document(record.creationDate.weeksSinceEpoch.toString()).updateStats()
        monthStatsRef.document(record.creationDate.monthSinceEpoch.toString()).updateStats()
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
