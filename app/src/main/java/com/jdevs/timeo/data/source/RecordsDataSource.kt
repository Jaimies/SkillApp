package com.jdevs.timeo.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.livedata.RecordListLiveData
import com.jdevs.timeo.util.RecordsConstants.FETCH_LIMIT
import com.jdevs.timeo.util.RecordsConstants.TIMESTAMP_PROPERTY

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object RecordsDataSource : RemoteDataSource() {

    override val liveData = ::RecordListLiveData
    private lateinit var recordsRef: CollectionReference

    override operator fun invoke(
        onLastItemCallback: () -> Unit,
        recordsRef: CollectionReference
    ): RecordsDataSource {

        super.reset(onLastItemCallback)
        this.recordsRef = recordsRef

        return this
    }

    override fun setRef(ref: CollectionReference) {

        recordsRef = ref
    }

    override fun createQuery(): Query {

        return recordsRef
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(FETCH_LIMIT)
    }
}
