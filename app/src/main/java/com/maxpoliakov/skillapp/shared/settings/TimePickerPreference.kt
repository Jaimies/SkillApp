package com.maxpoliakov.skillapp.shared.settings

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class TimePickerPreference : DialogPreference {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    var value: LocalTime
        get() = LocalTime.parse(getPersistedString("00:00"))
        set(value) {
            persistString(value.format(formatter))
            notifyChanged()
        }

    private val formatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun getSummary(): CharSequence? {
        if (summaryProvider != null) {
            return summaryProvider!!.provideSummary(this)
        }

        return value.format(formatter)
    }
}
