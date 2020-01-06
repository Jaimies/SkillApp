package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.FirestoreConstants.RECENT_RECORDS
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    override val activities: ItemsLiveData?

    override suspend fun saveActivity(activity: Activity): WriteBatch

    fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetActivitiesMonitor()
}

@Singleton
class ActivitiesRemoteDataSourceImpl @Inject constructor(
    authRepository: AuthRepository
) : FirestoreDataSource(authRepository), ActivitiesRemoteDataSource {

    private val activitiesMonitor =
        createCollectionMonitor(Activity::class.java, ActivitiesConstants.PAGE_SIZE)

    override val activities
        get() = activitiesMonitor.safeAccess().getLiveData()

    private var activitiesRef: CollectionReference by SafeInit()

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
        activitiesRef.add(activity)
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.documentId).delete()
    }

    override fun increaseTime(activityId: String, time: Long, batch: WriteBatch) {

        val activityRef = activitiesRef.document(activityId)
        val record = RecordMinimal(time)

        batch.update(
            activityRef,
            TOTAL_TIME,
            FieldValue.increment(time)
        )

        if (time > 0) {

            batch.update(
                activityRef,
                RECENT_RECORDS,
                FieldValue.arrayUnion(record)
            )
        }

        batch.commit()
    }

    override fun resetActivitiesMonitor() = activitiesMonitor.reset()

    override fun resetRefs(uid: String) {

        activitiesRef = createRef(uid, ActivitiesConstants.COLLECTION, activitiesMonitor)
    }
}
