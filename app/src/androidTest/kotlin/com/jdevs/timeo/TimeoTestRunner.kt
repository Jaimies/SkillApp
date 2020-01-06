package com.jdevs.timeo

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner

class TimeoTestRunner : AndroidJUnitRunner() {

    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?
    ): Application = super.newApplication(cl, TimeoTestApplication::class.java.name, context)
}
