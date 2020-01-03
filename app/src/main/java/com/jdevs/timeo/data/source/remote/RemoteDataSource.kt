package com.jdevs.timeo.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.RecordMinimal
import com.jdevs.timeo.data.WeekStats
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.data.source.TimeoDataSource
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

class RemoteDataSource(
    private val activitiesMonitor: CollectionMonitor,
    private val recordsMonitor: CollectionMonitor,
    private val dayStatsMonitor: CollectionMonitor,
    private val weekStatsMonitor: CollectionMonitor,
    private val monthStatsMonitor: CollectionMonitor,
    private val authRepository: AuthRepository
) : TimeoDataSource {

    override val activities: ItemsLiveData?
        get() {

            reset()
            return activitiesMonitor.getLiveData()
        }

    override val records: ItemsLiveData?
        get() {

            reset()
            return recordsMonitor.getLiveData()
        }

    override val dayStats: ItemsLiveData?
        get() {

            reset()
            return dayStatsMonitor.getLiveData()
        }

    override val weekStats: ItemsLiveData?
        get() {

            reset()
            return weekStatsMonitor.getLiveData()
        }
    override val monthStats: ItemsLiveData?
        get() {

            reset()
            return monthStatsMonitor.getLiveData()
        }

    private val firestore = FirebaseFirestore.getInstance()
    private var prevUid = ""
    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference
    private lateinit var dayStatsRef: CollectionReference
    private lateinit var weekStatsRef: CollectionReference
    private lateinit var monthStatsRef: CollectionReference

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

        suspend fun updateStats(vararg refs: DocumentReference) {

            refs.forEach { reference ->

                try {

                    reference.update("time", FieldValue.increment(record.time)).await()
                } catch (e: FirebaseFirestoreException) {

                    if (e.code == FirebaseFirestoreException.Code.NOT_FOUND) {

                        reference.set(WeekStats(record.time))
                    }
                }
            }
        }

        val dayStatsRef =
            dayStatsRef.document(record.creationDate.getDaysSinceEpoch().toString())
        val weekStatsRef =
            weekStatsRef.document(record.creationDate.getWeeksSinceEpoch().toString())
        val monthStatsRef =
            monthStatsRef.document(record.creationDate.getMonthSinceEpoch().toString())

        updateStats(dayStatsRef, weekStatsRef, monthStatsRef)
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

    override fun resetRecordsMonitor() = recordsMonitor.reset()
    override fun resetActivitiesMonitor() = activitiesMonitor.reset()
    override fun resetDayStatsMonitor() = dayStatsMonitor.reset()
    override fun resetWeekStatsMonitor() = weekStatsMonitor.reset()
    override fun resetMonthStatsMonitor() = monthStatsMonitor.reset()

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
