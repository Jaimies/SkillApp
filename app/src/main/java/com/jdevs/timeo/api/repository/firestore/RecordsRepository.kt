package com.jdevs.timeo.api.repository.firestore

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.jdevs.timeo.api.livedata.RecordListLiveData
import com.jdevs.timeo.util.ActivitiesConstants.TOTAL_TIME_PROPERTY
import com.jdevs.timeo.util.RecordsConstants.FETCH_LIMIT
import com.jdevs.timeo.util.RecordsConstants.TIMESTAMP_PROPERTY

object RecordsRepository : ItemListRepository() {

    override val liveData = ::RecordListLiveData

    operator fun invoke(onLastItemCallback: () -> Unit = {}): RecordsRepository {

        super.reset(onLastItemCallback)
        return this
    }

    override fun createQuery(): Query {

        return recordsRef
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(FETCH_LIMIT)
    }

    fun deleteRecord(id: String, recordTime: Long, activityId: String) {

        recordsRef.document(id).delete()

        activitiesRef.document(activityId)
            .update(TOTAL_TIME_PROPERTY, FieldValue.increment(-recordTime))
    }
}
