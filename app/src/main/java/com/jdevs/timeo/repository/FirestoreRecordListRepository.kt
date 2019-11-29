package com.jdevs.timeo.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.livedata.RecordListLiveData
import com.jdevs.timeo.util.ACTIVITIES_COLLECTION
import com.jdevs.timeo.util.ACTIVITIES_TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RECORDS_COLLECTION
import com.jdevs.timeo.util.RECORDS_FETCH_LIMIT
import com.jdevs.timeo.util.RECORDS_TIMESTAMP_PROPERTY
import com.jdevs.timeo.util.USERS_COLLECTION
import com.jdevs.timeo.viewmodel.RecordListViewModel

class FirestoreRecordListRepository(private val onLastItemReachedCallback: () -> Unit = {}) :
    RecordListViewModel.Repository {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    private val recordsRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$RECORDS_COLLECTION")
    }

    private val activitiesRef by lazy {
        firestore.collection("/$USERS_COLLECTION/${auth.currentUser?.uid}/$ACTIVITIES_COLLECTION")
    }

    private var query = recordsRef
        .orderBy(RECORDS_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
        .limit(RECORDS_FETCH_LIMIT)

    private var isLastItemReached = false
    private var lastVisibleRecord: DocumentSnapshot? = null

    override fun getRecordsListLiveData(): RecordListLiveData? {

        if (isLastItemReached) {

            return null
        }

        val lastRecord = lastVisibleRecord

        if (lastRecord != null) {
            query = query.startAfter(lastRecord)
        }

        return RecordListLiveData(query, ::setLastVisibleItem, ::onLastItemReached)
    }

    override fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(ACTIVITIES_TOTAL_TIME_PROPERTY, FieldValue.increment(-recordTime))
    }

    private fun onLastItemReached() {

        if(!isLastItemReached) {

            isLastItemReached = true
            onLastItemReachedCallback()
        }
    }

    private fun setLastVisibleItem(lastVisibleItem: DocumentSnapshot) {

        lastVisibleRecord = lastVisibleItem
    }
}
