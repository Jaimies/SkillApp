package com.maxpoliakov.skillapp

import android.app.Application
import com.maxpoliakov.skillapp.util.ui.setupTheme
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
open class TheApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        setupTheme()
    }
}
