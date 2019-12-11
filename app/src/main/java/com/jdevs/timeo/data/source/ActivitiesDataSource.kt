package com.jdevs.timeo.data.source

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.livedata.ActivityListLiveData
import com.jdevs.timeo.util.ActivitiesConstants.FETCH_LIMIT
import com.jdevs.timeo.util.ActivitiesConstants.TIMESTAMP_PROPERTY

@Suppress("PARAMETER_NAME_CHANGED_ON_OVERRIDE")
object ActivitiesDataSource : RemoteDataSource() {

    override val liveData = ::ActivityListLiveData
    private lateinit var activitiesRef: CollectionReference

    override operator fun invoke(
        onLastItemCallback: () -> Unit,
        activitiesRef: CollectionReference
    ): ActivitiesDataSource {

        super.reset(onLastItemCallback)
        this.activitiesRef = activitiesRef

        return this
    }

    override fun setRef(ref: CollectionReference) {

        activitiesRef = ref
    }

    override fun createQuery(): Query {

        return activitiesRef
            .orderBy(TIMESTAMP_PROPERTY, Query.Direction.DESCENDING)
            .limit(FETCH_LIMIT)
    }
}
