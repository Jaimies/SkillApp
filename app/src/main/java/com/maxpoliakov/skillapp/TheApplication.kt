package com.maxpoliakov.skillapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.maxpoliakov.skillapp.util.ui.setupTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class TheApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MobileAds.initialize(this)
        setupTheme()
    }
}
