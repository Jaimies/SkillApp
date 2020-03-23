package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.CollectionReference
import com.jdevs.timeo.data.ACTIVITIES_COLLECTION
import com.jdevs.timeo.data.ACTIVITY_ID
import com.jdevs.timeo.data.NAME
import com.jdevs.timeo.data.RECORDS_COLLECTION
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.createCollectionWatcher
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    fun getActivities(fetchNewItems: Boolean): List<LiveData<Operation<Activity>>>
}

@Singleton
class FirestoreActivitiesDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    ActivitiesRemoteDataSource {

    private val activitiesWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreActivity::mapToDomain)

    override fun getActivities(fetchNewItems: Boolean) =
        activitiesWatcher.safeAccess().getLiveDataList(fetchNewItems)

    private var activitiesRef: CollectionReference by SafeAccess()
    private var recordsRef: CollectionReference by SafeAccess()

    override fun getTopActivities() =
        activitiesRef.limit(TOP_ACTIVITIES_COUNT).watch(FirestoreActivity::mapToDomain)

    override fun getActivityById(id: String) =
        activitiesRef.document(id).watch(FirestoreActivity::mapToDomain)

    override suspend fun saveActivity(activity: Activity) {

        val querySnapshot = recordsRef
            .whereEqualTo(ACTIVITY_ID, activity.id)
            .get().await()

        db.runBatch { batch ->

            for (document in querySnapshot.documents) {

                batch.update(document.reference, NAME, activity.name)
            }

            val activityRef = activitiesRef.document(activity.id)
            batch.update(activityRef, NAME, activity.name)
        }
    }

    override suspend fun addActivity(name: String) {

        activitiesRef.add(FirestoreActivity(name = name))
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.id).delete()
    }

    override fun resetRefs(uid: String) {

        activitiesRef = createRef(uid, ACTIVITIES_COLLECTION, activitiesWatcher)
        recordsRef = createRef(uid, RECORDS_COLLECTION)
    }

    companion object {
        private const val TOP_ACTIVITIES_COUNT = 3L
        private const val PAGE_SIZE = 20L
    }
}
