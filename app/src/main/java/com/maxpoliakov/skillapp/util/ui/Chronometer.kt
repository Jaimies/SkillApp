package com.maxpoliakov.skillapp.util.ui

import android.os.SystemClock
import android.widget.Chronometer
import androidx.databinding.BindingAdapter
import java.time.Duration

@BindingAdapter("base")
fun Chronometer.setBase(base: Duration) {
    this.base = SystemClock.elapsedRealtime() - base.toMillis()
}

@BindingAdapter("isActive")
fun Chronometer.setIsActive(isActive: Boolean) {
    if (isActive) this.start()
    else this.stop()
}
