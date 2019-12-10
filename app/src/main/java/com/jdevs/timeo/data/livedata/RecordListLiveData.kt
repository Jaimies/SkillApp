package com.jdevs.timeo.data.livedata

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.util.RecordsConstants

class RecordListLiveData(
    query: Query?,
    setLastVisibleItem: (DocumentSnapshot) -> Unit,
    onLastItemReached: () -> Unit
) : ItemListLiveData(query, setLastVisibleItem, onLastItemReached) {

    override val dataType = Record::class.java
    override val fetchLimit = RecordsConstants.FETCH_LIMIT
}
