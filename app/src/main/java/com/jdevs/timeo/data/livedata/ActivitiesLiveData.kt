package com.jdevs.timeo.data.livedata

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.ActivitiesConstants

class ActivitiesLiveData(
    query: Query?,
    setLastVisibleItem: (DocumentSnapshot) -> Unit,
    onLastItemReached: () -> Unit
) : ItemsLiveData(query, setLastVisibleItem, onLastItemReached) {

    override val dataType = TimeoActivity::class.java
    override val fetchLimit = ActivitiesConstants.FETCH_LIMIT
}
