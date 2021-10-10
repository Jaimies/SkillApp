package com.maxpoliakov.skillapp.util.analytics

import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

fun Fragment.setAsCurrentScreen() {
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        val className = this@setAsCurrentScreen::class.java.simpleName
        param(FirebaseAnalytics.Param.SCREEN_NAME, className)
        param(FirebaseAnalytics.Param.SCREEN_CLASS, className)
    }
}
