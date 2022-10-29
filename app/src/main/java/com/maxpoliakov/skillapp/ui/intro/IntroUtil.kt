package com.maxpoliakov.skillapp.ui.intro

import com.maxpoliakov.skillapp.model.Intro

interface IntroUtil {
    fun showIfNecessary(intro: Intro, action: () -> Unit)
}
