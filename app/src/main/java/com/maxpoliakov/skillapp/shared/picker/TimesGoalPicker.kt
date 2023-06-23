package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R

class TimesGoalPicker : TimesPicker() {
    override val firstPickerEnabled = true

    class Builder : TimesPicker.Builder() {
        override var titleTextResId = R.string.select_goal
        override val titleTextInEditModeResId = R.string.select_goal

        override fun createDialog() = TimesGoalPicker()
    }
}
