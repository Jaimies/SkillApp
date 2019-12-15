package com.jdevs.timeo.data.source

import com.jdevs.timeo.data.livedata.RecordListLiveData
import com.jdevs.timeo.util.RecordsConstants

object RecordsDataSource : RemoteDataSource(RecordsConstants.FETCH_LIMIT, ::RecordListLiveData)
