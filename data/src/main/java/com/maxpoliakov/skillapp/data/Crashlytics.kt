package com.maxpoliakov.skillapp.data

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.logToCrashlytics() {
    FirebaseCrashlytics.getInstance().recordException(this)
}
