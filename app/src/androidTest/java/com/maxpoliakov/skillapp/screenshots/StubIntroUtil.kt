package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.model.Intro
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import javax.inject.Inject

class StubIntroUtil @Inject constructor(): IntroUtil {
    override fun showIfNecessary(intro: Intro, action: () -> Unit) {
        // do nothing
    }
}
