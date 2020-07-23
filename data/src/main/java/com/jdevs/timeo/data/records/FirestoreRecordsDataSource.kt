package com.jdevs.timeo.data.records

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.SetOptions
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
import com.jdevs.timeo.data.util.getCacheFirst
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
    private lateinit var recordsRef: CollectionReference
    private lateinit var activitiesRef: CollectionReference
    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference

    override fun getRecords(fetchNewItems: Boolean) = recordsWatcher.getLiveDataList(fetchNewItems)

    override suspend fun addRecord(record: Record) {

        val newRecordRef = recordsRef.document()

        db.runBatchSuspend { batch ->
            batch.set(newRecordRef, record.mapToFirestore())
            batch.increaseActivityTime(record.activityId, record.time, record.creationDate)
        }
    }

    override suspend fun deleteRecord(record: Record) {

        db.runBatchSuspend { batch ->
            batch.delete(recordsRef.document(record.id))
            batch.increaseActivityTime(record.activityId, -record.time, record.creationDate)
        }
    }

    private suspend fun WriteBatch.increaseActivityTime(
        activityId: String,
        time: Int,
        creationDate: OffsetDateTime,
        subActivityId: String = ""
    ) {

        val activityRef = activitiesRef.document(activityId)
        val activity = activityRef.getCacheFirst().toObject<FirestoreActivity>()!!
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

        handleSubactivities(updates, activity, time, subActivityId, creationDate)
        updateStats(time, activityId, creationDate)
        update(activityRef, updates)
    }

    private suspend fun WriteBatch.handleSubactivities(
        updates: MutableMap<String, Any>,
        activity: FirestoreActivity,
        time: Int,
        subActivityId: String,
        creationDate: OffsetDateTime
    ) {
        when {
            activity.parentActivity != null -> {
                updates["parentActivity.totalTime"] = increment(time)

                increaseActivityTime(
                    activity.parentActivity.id, time, creationDate, activity.documentId
                )
            }

            subActivityId != "" -> {

                val subactivities = activity.subActivities.toMutableList()

                subactivities.indexOfFirst { it.id == subActivityId }
                    .takeIf { it >= 0 }?.let { subActivityIndex ->

                        subactivities.update(subActivityIndex) { it.copy(totalTime = it.totalTime + time) }
                        updates["subActivities"] = subactivities
                    }
            }

            activity.subActivities.isNotEmpty() -> activity.subActivities.forEach {
                activitiesRef.document(it.id).update("parentActivity.totalTime", increment(time))
            }
        }
    }

    private fun WriteBatch.updateStats(
        time: Int, activityId: String,
        creationDate: OffsetDateTime
    ) {

        val timeIncrement = increment(time)

        fun updateStats(ref: CollectionReference, method: OffsetDateTime.() -> Number) {

            val day = creationDate.method()

            val changes = mapOf(
                TOTAL_TIME to timeIncrement, DAY to day,
                "activityTimes" to mapOf(activityId to timeIncrement)
            )

            set(ref.document(day.toString()), changes, SetOptions.merge())
        }

        updateStats(dayStatsRef) { daysSinceEpoch }
        updateStats(weekStatsRef) { weeksSinceEpoch }
        updateStats(monthStatsRef) { monthSinceEpoch }
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
