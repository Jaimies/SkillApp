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
    setTime(dateTime.until(getZonedDateTime()))
}

@BindingAdapter("time")
fun Chronometer.setTime(time: Duration?) {
    if (time == null) return
    this.base = SystemClock.elapsedRealtime() - time.toMillis()
}

@BindingAdapter("isActive")
fun Chronometer.setIsActive(isActive: Boolean) {
    if (isActive) this.start()
    else this.stop()
}
