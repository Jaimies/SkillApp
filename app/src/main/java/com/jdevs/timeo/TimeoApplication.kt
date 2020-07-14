package com.jdevs.timeo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.jdevs.timeo.data.setupFirebase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class TimeoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        setupFirebase()
    }
}
