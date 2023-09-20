package com.maxpoliakov.skillapp.data

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun logToCrashlytics(throwable: Throwable) {
    throwable.printStackTrace()
    FirebaseCrashlytics.getInstance().recordException(throwable)
}

fun Throwable.log() {
    logToCrashlytics(this)
}
