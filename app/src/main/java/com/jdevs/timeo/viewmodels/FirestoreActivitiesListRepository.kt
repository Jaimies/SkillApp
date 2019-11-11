package com.jdevs.timeo.viewmodels

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.livedata.ActivitiesListLiveData
import com.jdevs.timeo.utilities.ACTIVITIES_FETCH_LIMIT

class FirestoreActivitiesListRepository :
    ActivitiesListViewModel.ActivitiesListRepository,
    ActivitiesListLiveData.OnLastActivityReachedCallback,
    ActivitiesListLiveData.OnLastVisibleActivityCallback {

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

        val record = TimeoRecord(activityName, workingTime, activityId)

        recordsRef.add(record)

        activitiesRef.document(activityId)
            .update("totalTime", FieldValue.increment(workingTime.toLong()))
    }

    override fun setLastActivityReached(isLastActivityReached: Boolean) {
        this.isLastActivityReached = isLastActivityReached
    }

    override fun setLastVisibleActivity(lastVisibleActivity: DocumentSnapshot) {
        this.lastVisibleActivity = lastVisibleActivity
    }
}
