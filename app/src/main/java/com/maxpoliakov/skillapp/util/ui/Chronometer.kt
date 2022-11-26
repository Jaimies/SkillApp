package com.maxpoliakov.skillapp.util.ui

import android.os.SystemClock
import android.widget.Chronometer
import androidx.databinding.BindingAdapter
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import com.maxpoliakov.skillapp.shared.util.until
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZonedDateTime

@BindingAdapter("startTime")
fun Chronometer.setBase(dateTime: ZonedDateTime?) {
    if (dateTime == null) return
    this.base = dateTime.chronometerBase
}

@BindingAdapter("count")
fun Chronometer.setTime(count: Long?) {
    if (count == null) return
    this.base = Duration.ofMillis(count).chronometerBase
}

@BindingAdapter("count")
fun Chronometer.setTime(dateTime: LocalDateTime?) {
    if (dateTime == null) return

    val duration = dateTime.withHour(0).withMinute(0).until(dateTime)
    this.base = duration.chronometerBase
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
