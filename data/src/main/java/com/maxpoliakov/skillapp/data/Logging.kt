package com.maxpoliakov.skillapp.data

import com.google.firebase.crashlytics.FirebaseCrashlytics

fun Throwable.log() {
    this.printStackTrace()
    FirebaseCrashlytics.getInstance().recordException(this)
}
