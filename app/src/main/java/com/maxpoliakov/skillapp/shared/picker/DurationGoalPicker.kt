package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R

class DurationGoalPicker : DurationPicker() {
    override val firstPickerEnabled = true

    class Builder : DurationPicker.Builder() {
        override var titleTextResId = R.string.select_goal
        override val titleTextInEditModeResId = R.string.select_goal

        override fun createDialog() = DurationGoalPicker()
    }
}
