package com.jdevs.timeo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jdevs.timeo.di.DaggerAppComponent

open class TimeoApplication : Application() {
    val appComponent by lazy { initializeComponent() }

    protected open fun initializeComponent() =
        DaggerAppComponent.factory().create(applicationContext)

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
