package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.data.source.remote.ItemsLiveData.ActivitiesLiveData
import com.jdevs.timeo.data.source.remote.ItemsLiveData.RecordsLiveData
import com.jdevs.timeo.data.source.remote.RemoteDataSource
import com.jdevs.timeo.data.source.remote.RemoteRepository
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.RecordsConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class TimeoApplication : Application() {

    val repository
        get() = ServiceLocator.provideLocalRepository(this)

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
