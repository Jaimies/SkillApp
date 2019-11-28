package com.jdevs.timeo.repository

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.livedata.ActivityListLiveData
import com.jdevs.timeo.util.ACTIVITIES_ACTIVITY_ID_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_COLLECTION
import com.jdevs.timeo.util.ACTIVITIES_FETCH_LIMIT
import com.jdevs.timeo.util.ACTIVITIES_NAME_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.ACTIVITIES_TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RECORDS_COLLECTION
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.USERS_COLLECTION
import com.jdevs.timeo.viewmodel.ActivitiesListViewModel

class FirestoreActivitiesListRepository :
    ActivitiesListViewModel.ActivitiesListRepository,
    ActivityListLiveData.OnLastActivityReachedCallback,
    ActivityListLiveData.OnLastVisibleActivityCallback,
    OnFailureListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val activitiesRef by lazy {
        firestore
            .collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$ACTIVITIES_COLLECTION")
    }

    private val recordsRef by lazy {
        firestore
            .collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$RECORDS_COLLECTION")
    }

    private var query = activitiesRef
        .orderBy(ACTIVITIES_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
        .limit(ACTIVITIES_FETCH_LIMIT)

    private var isLastActivityReached = false
    private var lastVisibleActivity: DocumentSnapshot? = null

    override fun getActivitiesListLiveData(): ActivityListLiveData? {

        if (isLastActivityReached) {
            return null
        }

        val lastActivity = lastVisibleActivity

        if (lastActivity != null) {
            query = query.startAfter(lastActivity)
        }

        return ActivityListLiveData(query, this, this)
    }

    override fun createRecord(activityName: String, time: Long, activityId: String) {

        val record = Record(activityName, time, activityId)

        recordsRef.add(record)

        activitiesRef.document(activityId)
            .update(ACTIVITIES_TOTAL_TIME_PROPERTY, FieldValue.increment(time))
            .addOnFailureListener(this)
    }

    override fun updateActivity(activity: TimeoActivity, activityId: String) {

        val activityReference = activitiesRef.document(activityId)

        recordsRef.whereEqualTo(ACTIVITIES_ACTIVITY_ID_PROPERTY, activityId).get()
            .addOnSuccessListener { querySnapshot ->

                val recordReferences = ArrayList<DocumentReference>()

                for (record in querySnapshot.documents) {

                    recordReferences.add(record.reference)
                }

                firestore.runBatch { batch ->

                    batch.set(activityReference, activity)

                    for (recordReference in recordReferences) {

                        batch.update(recordReference, ACTIVITIES_NAME_PROPERTY, activity.name)
                    }
                }
                    .addOnFailureListener(this)
            }
    }

    override fun createActivity(activity: TimeoActivity) {

        activitiesRef.add(activity)
            .addOnFailureListener(this)
    }

    override fun deleteActivity(activityId: String) {

        activitiesRef.document(activityId).delete().addOnFailureListener(this)
    }

    override fun onFailure(exception: Exception) {

        Log.w(
            TAG,
            "Failed to save data to Firestore",
            exception
        )
    }

    override fun setLastActivityReached(isLastActivityReached: Boolean) {
        this.isLastActivityReached = isLastActivityReached
    }

    override fun setLastVisibleActivity(lastVisibleActivity: DocumentSnapshot) {
        this.lastVisibleActivity = lastVisibleActivity
    }
}
