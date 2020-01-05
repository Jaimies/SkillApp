package com.jdevs.timeo.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordMinimal
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.NAME_PROPERTY
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.StatsConstants
import com.jdevs.timeo.util.await
import com.jdevs.timeo.util.logOnFailure
import com.jdevs.timeo.util.time.getDaysSinceEpoch
import com.jdevs.timeo.util.time.getMonthSinceEpoch
import com.jdevs.timeo.util.time.getWeeksSinceEpoch

class RemoteDataSourceImpl(
    private val activitiesMonitor: CollectionMonitor,
    private val recordsMonitor: CollectionMonitor,
    private val dayStatsMonitor: CollectionMonitor,
    private val weekStatsMonitor: CollectionMonitor,
    private val monthStatsMonitor: CollectionMonitor,
    private val authRepository: AuthRepository
) : RemoteDataSource {

    override val activities
        get() = activitiesMonitor.getLiveData().also { reset() }

    override val records
        get() = recordsMonitor.getLiveData().also { reset() }

    override val dayStats
        get() = dayStatsMonitor.getLiveData().also { reset() }

    override val weekStats
        get() = weekStatsMonitor.getLiveData().also { reset() }

    override val monthStats
        get() = monthStatsMonitor.getLiveData().also { reset() }

    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference
    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference
    private var prevUid = ""
    private val firestore = FirebaseFirestore.getInstance()

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        val activity = activitiesRef.document(documentId)
        val liveData = MutableLiveData<Activity>()

        activity.addSnapshotListener { documentSnapshot, _ ->

            if (documentSnapshot != null) {

                liveData.value = documentSnapshot.toObject(Activity::class.java)?.apply {

                    setupTimestamp()
                    setupLastWeekTime()
                }
            }
        }

        return liveData
    }

    override suspend fun addRecord(record: Record) {

        record.setupFirestoreTimestamp()

        val newRecordRef = recordsRef.document()
        val activityRef = activitiesRef.document(record.activityId)

        firestore.runBatch { batch ->

            batch.set(newRecordRef, record)

            batch.update(activityRef, TOTAL_TIME_PROPERTY, FieldValue.increment(record.time))

            batch.update(
                activityRef,
                "recentRecords",
                FieldValue.arrayUnion(RecordMinimal(record.time))
            )
        }

        updateStats(record)
    }

    override suspend fun saveActivity(activity: Activity) {

        val activityRef = activitiesRef.document(activity.documentId)

        activity.setupFirestoreTimestamp()

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

        activity.setupFirestoreTimestamp()

        activitiesRef.add(activity)
            .logOnFailure("Failed to add data to Firestore")
    }

    override suspend fun deleteActivity(activity: Activity) =
        activitiesRef.document(activity.documentId).delete()
            .logOnFailure("Failed to delete data from Firestore")

    override suspend fun deleteRecord(record: Record) {

        record.setupFirestoreTimestamp()

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

        updateStats(record, true)
    }

    override fun resetRecordsMonitor() = recordsMonitor.reset()
    override fun resetActivitiesMonitor() = activitiesMonitor.reset()
    override fun resetDayStatsMonitor() = dayStatsMonitor.reset()
    override fun resetWeekStatsMonitor() = weekStatsMonitor.reset()
    override fun resetMonthStatsMonitor() = monthStatsMonitor.reset()

    private suspend fun updateStats(record: Record, decrement: Boolean = false) {

        val refs = listOf(
            dayStatsRef.document(record.creationDate.getDaysSinceEpoch().toString()),
            weekStatsRef.document(record.creationDate.getWeeksSinceEpoch().toString()),
            monthStatsRef.document(record.creationDate.getMonthSinceEpoch().toString())
        )

        val time = if (!decrement) record.time else -record.time

        refs.forEach { ref ->

            try {

                ref.update("time", FieldValue.increment(time)).await()
            } catch (e: FirebaseFirestoreException) {

                if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                    ref.set(DayStats(time, ref.id.toLong()))
                }
            }
        }
    }

    private fun reset() {

        val uid = authRepository.uid ?: return

        if (uid == prevUid) {

            return
        }

        fun createRef(collection: String, monitor: CollectionMonitor) = firestore
            .collection("/$USERS_COLLECTION/$uid/$collection").also {

                monitor.setRef(it)
            }

        activitiesRef = createRef(ActivitiesConstants.COLLECTION, activitiesMonitor)
        recordsRef = createRef(RecordsConstants.COLLECTION, recordsMonitor)
        dayStatsRef = createRef(StatsConstants.DAY_STATS_COLLECTION, dayStatsMonitor)
        weekStatsRef = createRef(StatsConstants.WEEK_STATS_COLLECTION, weekStatsMonitor)
        monthStatsRef = createRef(StatsConstants.MONTH_STATS_COLLECTION, monthStatsMonitor)

        prevUid = uid
    }

    companion object {

        private const val USERS_COLLECTION = "users"
    }
}
