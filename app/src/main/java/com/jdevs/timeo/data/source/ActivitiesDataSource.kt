package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.livedata.ActivityListLiveData
import com.jdevs.timeo.util.ActivitiesConstants

object ActivitiesDataSource :
    RemoteDataSource(ActivitiesConstants.FETCH_LIMIT, ::ActivityListLiveData)
