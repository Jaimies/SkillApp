package com.jdevs.timeo.repositories

import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.livedata.ActivitiesListLiveData
import com.jdevs.timeo.utils.ACTIVITIES_FETCH_LIMIT
import com.jdevs.timeo.utils.TAG
import com.jdevs.timeo.viewmodels.ActivitiesListViewModel

class FirestoreActivitiesListRepository :
    ActivitiesListViewModel.ActivitiesListRepository,
    ActivitiesListLiveData.OnLastActivityReachedCallback,
    ActivitiesListLiveData.OnLastVisibleActivityCallback,
    OnFailureListener {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val activitiesRef = firestore.collection("/users/${auth.currentUser!!.uid}/activities")
    private val recordsRef = firestore.collection("/users/${auth.currentUser!!.uid}/records")
    private var query = activitiesRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(
        ACTIVITIES_FETCH_LIMIT
    )
    private var isLastActivityReached = false
    private var lastVisibleActivity: DocumentSnapshot? = null

    override fun getActivitiesListLiveData(): ActivitiesListLiveData? {

        if (isLastActivityReached) {
            return null
        }

        val lastActivity = lastVisibleActivity

        if (lastActivity != null) {
            query = query.startAfter(lastActivity)
        }

        return ActivitiesListLiveData(query, this, this)
    }

    override fun createRecord(activityName: String, workingTime: Int, activityId: String) {

        val record = Record(activityName, workingTime, activityId)

        recordsRef.add(record)

        activitiesRef.document(activityId)
            .update("totalTime", FieldValue.increment(workingTime.toLong()))
            .addOnFailureListener(this)
    }

    override fun updateActivity(activity: TimeoActivity, activityId: String) {

        val activityReference = activitiesRef.document(activityId)

        recordsRef.whereEqualTo("activityId", activityId).get()
            .addOnSuccessListener { querySnapshot ->

                val recordReferences = ArrayList<DocumentReference>()

                for (record in querySnapshot.documents) {

                    recordReferences.add(record.reference)
                }

                firestore.runBatch { batch ->

                    batch.set(activityReference, activity)

                    for (recordReference in recordReferences) {

                        batch.update(recordReference, "title", activity.title)
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
