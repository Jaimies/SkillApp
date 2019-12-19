package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.data.ItemsLiveData.ActivitiesLiveData
import com.jdevs.timeo.data.ItemsLiveData.RecordsLiveData
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
            RemoteDataSource(ActivitiesConstants.FETCH_LIMIT, ::ActivitiesLiveData),
            RemoteDataSource(RecordsConstants.FETCH_LIMIT, ::RecordsLiveData)
        )
    }

    fun onDestroy() {

        ioScope.coroutineContext.cancel()
    }
}
