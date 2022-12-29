package com.maxpoliakov.skillapp.util.analytics

import androidx.fragment.app.Fragment
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

fun logCurrentScreenToAnalytics(fragment: Fragment) {
    Firebase.analytics.logEvent(FirebaseAnalytics.Event.SCREEN_VIEW) {
        val className = fragment::class.java.simpleName
        param(FirebaseAnalytics.Param.SCREEN_NAME, className)
        param(FirebaseAnalytics.Param.SCREEN_CLASS, className)
    }
}

fun logEvent(message: String) {
    Firebase.analytics.logEvent(message) {}
}
