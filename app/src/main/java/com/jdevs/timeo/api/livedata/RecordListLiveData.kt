package com.jdevs.timeo.api.livedata

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.data.operations.Operation
import com.jdevs.timeo.data.operations.RecordOperation
import com.jdevs.timeo.util.RecordsConstants

class RecordListLiveData(
    query: Query?,
    setLastVisibleItem: (DocumentSnapshot) -> Unit,
    onLastItemReached: () -> Unit
) : ItemListLiveData(query, setLastVisibleItem, onLastItemReached) {

    override val dataType = Record::class.java
    override val operation = ::RecordOperation as (Any?, Int, String) -> Operation
    override val fetchLimit = RecordsConstants.FETCH_LIMIT
}
