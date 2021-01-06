package com.maxpoliakov.skillapp.util.ui

import android.os.SystemClock
import android.widget.Chronometer
import androidx.databinding.BindingAdapter
import com.maxpoliakov.skillapp.shared.util.getZonedDateTime
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit.MILLIS

@BindingAdapter("startTime")
fun Chronometer.setBase(dateTime: ZonedDateTime) {
    val millisSince = dateTime.until(getZonedDateTime(), MILLIS)
    this.base = SystemClock.elapsedRealtime() - millisSince
}

@BindingAdapter("isActive")
fun Chronometer.setIsActive(isActive: Boolean) {
    if (isActive) this.start()
    else this.stop()
}
