package com.jdevs.timeo.api.repository.firestore

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.jdevs.timeo.api.livedata.RecordListLiveData
import com.jdevs.timeo.ui.history.viewmodel.HistoryViewModel
import com.jdevs.timeo.util.ActivitiesConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants.FETCH_LIMIT
import com.jdevs.timeo.util.RecordsConstants.TIMESTAMP_PROPERTY

class RecordsRepository(onLastItemCallback: () -> Unit = {}) :
    FirestoreItemListRepository(onLastItemCallback),
    HistoryViewModel.Repository {

    override var initialQuery = recordsRef
        .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
        .limit(FETCH_LIMIT)

    override var query = initialQuery
    override val liveDataConstructor = ::RecordListLiveData

    override fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(TOTAL_TIME_PROPERTY, FieldValue.increment(-recordTime))
    }
}
