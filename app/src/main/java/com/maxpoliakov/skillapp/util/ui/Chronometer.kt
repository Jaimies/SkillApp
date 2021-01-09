package com.maxpoliakov.skillapp.util.ui

import android.os.SystemClock
import android.widget.Chronometer
import androidx.databinding.BindingAdapter
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.until
import java.time.Duration
import java.time.ZonedDateTime

@BindingAdapter("startTime")
fun Chronometer.setBase(dateTime: ZonedDateTime) {
    this.base = dateTime.chronometerBase
}

@BindingAdapter("time")
fun Chronometer.setTime(time: Duration?) {
    if (time == null) return
    this.base = time.chronometerBase
}

val ZonedDateTime.chronometerBase: Long
    get() = this.until(getZonedDateTime()).chronometerBase

val Duration.chronometerBase: Long
    get() = SystemClock.elapsedRealtime() - this.toMillis()

@BindingAdapter("isActive")
fun Chronometer.setIsActive(isActive: Boolean) {
    if (isActive) this.start()
    else this.stop()
}
