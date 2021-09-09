package com.maxpoliakov.skillapp.util.error

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.logToCrashlytics() {
    FirebaseCrashlytics.getInstance().recordException(this)
}
