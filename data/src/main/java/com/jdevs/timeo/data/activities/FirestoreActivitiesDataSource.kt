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
import com.jdevs.timeo.data.firestore.watchCollection
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    val activities: List<LiveData<Operation<Activity>>>
}

@Singleton
class FirestoreActivitiesDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    ActivitiesRemoteDataSource {

    private val activitiesWatcher =
        createCollectionWatcher(PAGE_SIZE, FirestoreActivity::mapToDomain)

    override val activities get() = activitiesWatcher.getLiveDataList()

    private var activitiesRef: CollectionReference by SafeAccess()
    private var recordsRef: CollectionReference by SafeAccess()

    override fun getTopActivities() =
        activitiesRef.watchCollection(FirestoreActivity::mapToDomain, TOP_ACTIVITIES_COUNT)

    override fun getActivityById(id: Int, documentId: String) =
        activitiesRef.document(documentId).watch(FirestoreActivity::mapToDomain)

    override suspend fun saveActivity(activity: Activity) = withContext<Unit>(Dispatchers.IO) {

        db.runBatch { batch ->

            launch {

                val querySnapshot = recordsRef
                    .whereEqualTo(ACTIVITY_ID, activity.documentId)
                    .get().await()

                for (document in querySnapshot.documents) {

                    batch.update(document.reference, NAME, activity.name)
                }

                val activityRef = activitiesRef.document(activity.documentId)
                batch.update(activityRef, NAME, activity.name)
            }
        }
    }

    override suspend fun addActivity(name: String) {

        activitiesRef.add(FirestoreActivity(name = name))
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.documentId).delete()
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
