package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.WriteBatch
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.ItemsLiveData
import com.jdevs.timeo.data.firestore.RecordMinimal
import com.jdevs.timeo.data.firestore.createCollectionMonitor
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.repository.AuthRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.ActivitiesConstants.ACTIVITIES_FIRESTORE_PAGE_SIZE
import com.jdevs.timeo.util.ActivitiesConstants.TOP_ACTIVITIES_COUNT
import com.jdevs.timeo.util.FirestoreConstants.NAME
import com.jdevs.timeo.util.FirestoreConstants.RECENT_RECORDS
import com.jdevs.timeo.util.FirestoreConstants.TOTAL_TIME
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    override val activities: ItemsLiveData?

    suspend fun saveActivity(id: String, newName: String): WriteBatch

    fun increaseTime(activityId: String, time: Long, batch: WriteBatch)

    fun resetMonitor()
}

@Singleton
class FirestoreActivitiesDataSource @Inject constructor(
    authRepository: AuthRepository,
    private val mapper: FirestoreActivityMapper,
    private val domainMapper: FirestoreDomainMapper
) : FirestoreListDataSource(authRepository),
    ActivitiesRemoteDataSource {

    private val activitiesMonitor =
        createCollectionMonitor(
            FirestoreActivity::class,
            ACTIVITIES_FIRESTORE_PAGE_SIZE,
            domainMapper
        )

    override val activities
        get() = activitiesMonitor.safeAccess().getLiveData()

    private var activitiesRef: CollectionReference by SafeAccess()

    override fun getTopActivities(): LiveData<List<Activity>> {

        val liveData = MutableLiveData<List<Activity>>()

        activitiesRef.orderBy(TOTAL_TIME, Query.Direction.DESCENDING).limit(TOP_ACTIVITIES_COUNT)
            .addSnapshotListener { querySnapshot, _ ->

                querySnapshot?.documents?.mapNotNull {
                    it.toObject(FirestoreActivity::class.java)
                }?.let { liveData.value = it.map(domainMapper::map) }
            }

        return liveData
    }

    override fun getActivityById(id: Int, documentId: String): LiveData<Activity> {

        val activity = activitiesRef.document(documentId)
        val liveData = MutableLiveData<Activity>()

        activity.addSnapshotListener { documentSnapshot, _ ->

            documentSnapshot?.toObject(FirestoreActivity::class.java)?.let {
                liveData.value = domainMapper.map(it)
            }
        }

        return liveData
    }

    override suspend fun saveActivity(id: String, newName: String): WriteBatch {

        val activityRef = activitiesRef.document(id)

        return db.batch().also { batch ->

            batch.update(activityRef, NAME, newName)
        }
    }

    override suspend fun addActivity(activity: Activity) {

        activitiesRef.add(mapper.map(activity))
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.documentId).delete()
    }

    override fun increaseTime(activityId: String, time: Long, batch: WriteBatch) {

        val activityRef = activitiesRef.document(activityId)
        val record = RecordMinimal(time.toInt())

        batch.update(activityRef, TOTAL_TIME, FieldValue.increment(time))

        if (time > 0) {

            batch.update(activityRef, RECENT_RECORDS, FieldValue.arrayUnion(record))
        }

        batch.commit()
    }

    override fun resetMonitor() = activitiesMonitor.reset()

    override fun resetRefs(uid: String) {

        activitiesRef = createRef(uid, ActivitiesConstants.COLLECTION, activitiesMonitor)
    }
}
