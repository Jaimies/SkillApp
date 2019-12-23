package com.jdevs.timeo

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class TimeoApplication : Application() {

    val repository
        get() = ServiceLocator.provideRepository(this)

    val ioScope = CoroutineScope(Dispatchers.IO + Job())

    fun onDestroy() {

        ioScope.coroutineContext.cancel()
    }
}
