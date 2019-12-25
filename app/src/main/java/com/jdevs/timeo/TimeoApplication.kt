package com.jdevs.timeo

import android.app.Application
import com.jdevs.timeo.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class TimeoApplication : Application() {

    val appComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }

    val ioScope = CoroutineScope(Dispatchers.IO + Job())

    fun onDestroy() {

        ioScope.coroutineContext.cancel()
    }
}
