package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.data.livedata.ActivityListLiveData
import com.jdevs.timeo.data.livedata.RecordListLiveData
import com.jdevs.timeo.data.source.RemoteDataSource
import com.jdevs.timeo.data.source.RemoteRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class TimeoApplication : Application() {

    val ioScope = CoroutineScope(Dispatchers.IO + Job())

    override fun onCreate() {

        super.onCreate()

        RemoteRepository.initialize(
            RemoteDataSource(ActivitiesConstants.FETCH_LIMIT, ::ActivityListLiveData),
            RemoteDataSource(RecordsConstants.FETCH_LIMIT, ::RecordListLiveData)
        )
    }

    fun onDestroy() {

        ioScope.coroutineContext.cancel()
    }
}
