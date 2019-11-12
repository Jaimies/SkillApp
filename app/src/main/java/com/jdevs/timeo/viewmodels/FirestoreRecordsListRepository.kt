package com.jdevs.timeo.viewmodels

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.livedata.RecordsListLiveData
import com.jdevs.timeo.utilities.RECORDS_FETCH_LIMIT

class FirestoreRecordsListRepository :
    RecordsListViewModel.RecordsListRepository,
    RecordsListLiveData.OnLastRecordReachedCallback,
    RecordsListLiveData.OnLastVisibleRecordCallback {

    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val auth by lazy { FirebaseAuth.getInstance() }

    private val recordsRef by lazy { firestore.collection("/users/${auth.currentUser!!.uid}/records") }
    private val activitiesRef by lazy { firestore.collection("/users/${auth.currentUser!!.uid}/activities") }

    private var query = recordsRef.orderBy("timestamp", Query.Direction.DESCENDING).limit(
        RECORDS_FETCH_LIMIT
    )

    private var isLastRecordReached = false
    private var lastVisibleRecord: DocumentSnapshot? = null

    override fun getRecordsListLiveData(): RecordsListLiveData? {

        if (isLastRecordReached) {
            return null
        }

        val lastRecord = lastVisibleRecord

        if (lastRecord != null) {
            query = query.startAfter(lastRecord)
        }

        return RecordsListLiveData(query, this, this)
    }

    override fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(
                "totalTime",
                FieldValue.increment(-recordTime)
            )
    }

    override fun setLastRecordReached(isLastRecordReached: Boolean) {
        this.isLastRecordReached = isLastRecordReached
    }

    override fun setLastVisibleRecord(lastVisibleRecord: DocumentSnapshot) {
        this.lastVisibleRecord = lastVisibleRecord
    }
}
