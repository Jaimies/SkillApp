package com.maxpoliakov.skillapp.shared.picker

import com.maxpoliakov.skillapp.R

class PageCountGoalPicker : PageCountPicker() {
    override val firstPickerEnabled = true

    class Builder : PageCountPicker.Builder() {
        override var titleTextResId = R.string.select_goal
        override val titleTextInEditModeResId = R.string.select_goal

        override fun createDialog() = PageCountGoalPicker()
    }
}
