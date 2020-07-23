package com.jdevs.timeo.data.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue.arrayUnion
import com.google.firebase.firestore.Query.Direction.DESCENDING
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.Source.CACHE
import com.google.firebase.firestore.Source.SERVER
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.jdevs.timeo.data.ACTIVITIES_COLLECTION
import com.jdevs.timeo.data.ACTIVITY_ID
import com.jdevs.timeo.data.NAME
import com.jdevs.timeo.data.RECORDS_COLLECTION
import com.jdevs.timeo.data.TOTAL_TIME
import com.jdevs.timeo.data.firestore.FirestoreListDataSource
import com.jdevs.timeo.data.firestore.QueryWatcher
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.data.util.getCacheFirst
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.Operation
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

interface ActivitiesRemoteDataSource : ActivitiesDataSource {

    fun getActivities(fetchNewItems: Boolean): List<LiveData<Operation<Activity>>>
    fun getTopLevelActivitiesFromCache(activityIdToExclude: String): LiveData<List<Activity>>
}

@Singleton
class FirestoreActivitiesDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreListDataSource(authRepository),
    ActivitiesRemoteDataSource {

    private val functions = Firebase.functions("europe-west1")
    private val activitiesWatcher = QueryWatcher(PAGE_SIZE, FirestoreActivity::mapToDomain)
    private lateinit var activitiesRef: CollectionReference
    private lateinit var recordsRef: CollectionReference

    override fun getActivities(fetchNewItems: Boolean) =
        activitiesWatcher.getLiveDataList(fetchNewItems)

    override fun getTopActivities() =
        activitiesRef.orderBy(TOTAL_TIME, DESCENDING).limit(TOP_ACTIVITIES_COUNT)
            .watch(FirestoreActivity::mapToDomain)

    override fun getActivityById(id: String) =
        activitiesRef.document(id).watch(FirestoreActivity::mapToDomain)

    override fun getTopLevelActivitiesFromCache(activityIdToExclude: String): LiveData<List<Activity>> {

        val liveData = MutableLiveData<List<Activity>>()

        val query = activitiesRef.whereEqualTo("isTopLevel", true)
            .orderBy(TOTAL_TIME, DESCENDING)

        val listener = { snapshot: QuerySnapshot ->
            liveData.value = snapshot.toObjects<FirestoreActivity>()
                .map { it.mapToDomain() }
                .filter { it.id != activityIdToExclude }
        }

        query.get(CACHE).addOnSuccessListener(listener).addOnFailureListener {
            query.get(SERVER).addOnSuccessListener(listener)
        }

        return liveData
    }

    override suspend fun saveActivity(activity: Activity) {

        val activityRef = activitiesRef.document(activity.id)

        activityRef.update(
            "parentActivity", activity.parentActivity?.mapToFirestore(),
            "isTopLevel", activity.parentActivity == null
        )

        val oldActivity = activityRef.getCacheFirst().toObject<FirestoreActivity>()!!

        if (oldActivity.name != activity.name) {
            val querySnapshot = recordsRef
                .whereEqualTo(ACTIVITY_ID, activity.id)
                .get().await()

            db.runBatch { batch ->
                for (document in querySnapshot.documents) {
                    batch.update(document.reference, NAME, activity.name)
                }

                batch.update(activityRef, NAME, activity.name)
            }
        }
    }

    override suspend fun addActivity(activity: Activity) {

        val activityRef = activitiesRef.document().also { it.set(activity.mapToFirestore()) }

        if (activity.parentActivity != null) {
            activitiesRef.document(activity.parentActivity!!.id)
                .update("subActivities", arrayUnion(activity.toFirestoreMinimal(activityRef.id)))
        }
    }

    override suspend fun deleteActivity(activity: Activity) {

        activitiesRef.document(activity.id).delete()
        functions.getHttpsCallable("deleteRecords").call(mapOf("activityId" to activity.id))
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
