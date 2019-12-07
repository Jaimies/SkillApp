package com.jdevs.timeo.api.livedata

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.operations.ActivityOperation
import com.jdevs.timeo.data.operations.Operation

class ActivityListLiveData(
    query: Query?,
    setLastVisibleItem: (DocumentSnapshot) -> Unit,
    onLastItemReached: () -> Unit
) : ItemListLiveData(query, setLastVisibleItem, onLastItemReached) {

    override val dataType = TimeoActivity::class.java
    override val operationConstructor = ::ActivityOperation as (Any?, Int, String) -> Operation
}
