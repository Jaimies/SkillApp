package com.jdevs.timeo

import com.jdevs.timeo.di.AppComponent
import com.jdevs.timeo.di.DaggerTestAppComponent

class TimeoTestApplication : TimeoApplication() {

    override fun initializeComponent(): AppComponent = DaggerTestAppComponent.create()
}
