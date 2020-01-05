package com.jdevs.timeo.data.source.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.RecordMinimal
import com.jdevs.timeo.data.source.AuthRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants
import com.jdevs.timeo.util.RecordsConstants
import com.jdevs.timeo.util.logOnFailure
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ActivitiesRemoteDataSourceImpl @Inject constructor(
    authRepository: AuthRepository
) : BaseRemoteDataSource(authRepository), ActivitiesRemoteDataSource {

    private val activitiesMonitor =
        createCollectionMonitor(Activity::class.java, ActivitiesConstants.PAGE_SIZE)

    override val activities
        get() = activitiesMonitor.getLiveData().also { reset() }

    private lateinit var activitiesRef: CollectionReference

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

    override suspend fun saveActivity(activity: Activity): WriteBatch {

        val activityRef = activitiesRef.document(activity.documentId)

        activity.setupFirestoreTimestamp()

        return db.batch().also { batch ->

            batch.set(activityRef, activity)
        }
    }

    override suspend fun addActivity(activity: Activity) {

        activity.setupFirestoreTimestamp()
        activitiesRef.add(activity).logOnFailure("Failed to add data to Firestore")
    }

    override suspend fun deleteActivity(activity: Activity) =
        activitiesRef.document(activity.documentId).delete()
            .logOnFailure("Failed to delete data from Firestore")

    override fun increaseTime(activityId: String, time: Long, batch: WriteBatch) {

        val activityRef = activitiesRef.document(activityId)
        val record = RecordMinimal(time)

        batch.update(
            activityRef,
            FirestoreConstants.TOTAL_TIME_PROPERTY,
            FieldValue.increment(time)
        )

        batch.update(
            activityRef,
            "recentRecords",
            if (time > 0) FieldValue.arrayUnion(record) else FieldValue.arrayRemove(record)
        )

        batch.commit()
    }

    override fun resetActivitiesMonitor() = activitiesMonitor.reset()

    override fun resetRefs(uid: String) {

        activitiesRef = createRef(uid, RecordsConstants.COLLECTION, activitiesMonitor)
    }
}
