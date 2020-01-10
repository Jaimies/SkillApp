package com.jdevs.timeo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jdevs.timeo.di.DaggerAppComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

open class TimeoApplication : Application() {

    val ioScope = CoroutineScope(Dispatchers.IO + Job())
    val appComponent by lazy { initializeComponent() }

    protected open fun initializeComponent() =
        DaggerAppComponent.factory().create(applicationContext)

    override fun onCreate() {

        super.onCreate()
        AndroidThreeTen.init(this)
    }

    fun onDestroy() = ioScope.coroutineContext.cancel()
}
