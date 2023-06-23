package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R

class DistanceGoalPicker : DistancePicker() {
    override val firstPickerEnabled get() = true

    class Builder : DistancePicker.Builder() {
        override var titleTextResId = R.string.select_goal
        override val titleTextInEditModeResId = R.string.select_goal

        override fun createDialog() = DistanceGoalPicker()
    }
}
