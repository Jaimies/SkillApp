package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.data.activitiesLiveData
import com.jdevs.timeo.data.recordsLiveData
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
            RemoteDataSource(ActivitiesConstants.FETCH_LIMIT, ::activitiesLiveData),
            RemoteDataSource(RecordsConstants.FETCH_LIMIT, ::recordsLiveData)
        )
    }

    fun onDestroy() {

        ioScope.coroutineContext.cancel()
    }
}
