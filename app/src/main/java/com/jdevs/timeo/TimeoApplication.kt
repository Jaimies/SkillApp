package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.data.source.ActivitiesDataSource
import com.jdevs.timeo.data.source.RecordsDataSource
import com.jdevs.timeo.data.source.RemoteRepository

class TimeoApplication : Application() {

    override fun onCreate() {

        super.onCreate()
        RemoteRepository.initialize(ActivitiesDataSource, RecordsDataSource)
    }
}
