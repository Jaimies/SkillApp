package com.jdevs.timeo.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.jdevs.timeo.livedata.RecordListLiveData
import com.jdevs.timeo.repository.common.BaseFirestoreRepository
import com.jdevs.timeo.util.ACTIVITIES_TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RECORDS_FETCH_LIMIT
import com.jdevs.timeo.util.RECORDS_TIMESTAMP_PROPERTY
import com.jdevs.timeo.viewmodel.RecordListViewModel

class FirestoreRecordListRepository(onLastItemCallback: () -> Unit = {}) :
    BaseFirestoreRepository(onLastItemCallback),
    RecordListViewModel.Repository {

    override var initialQuery = recordsRef
        .orderBy(RECORDS_TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
        .limit(RECORDS_FETCH_LIMIT)

    override var query = initialQuery

    override val liveDataConstructor = ::RecordListLiveData

    override fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(ACTIVITIES_TOTAL_TIME_PROPERTY, FieldValue.increment(-recordTime))
    }
}
