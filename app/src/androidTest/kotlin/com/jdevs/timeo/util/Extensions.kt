package com.jdevs.timeo.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.test.platform.app.InstrumentationRegistry
import com.jdevs.timeo.TimeoTestApplication
import com.jdevs.timeo.di.TestAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
val testAppComponent: TestAppComponent
    get() {

        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val app = instrumentation.targetContext.applicationContext as TimeoTestApplication

        return app.appComponent as TestAppComponent
    }

fun <T> createLiveData() = MutableLiveData<T>() as LiveData<T>

