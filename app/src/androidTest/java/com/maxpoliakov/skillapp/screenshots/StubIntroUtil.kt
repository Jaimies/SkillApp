package com.maxpoliakov.skillapp.screenshots

import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import javax.inject.Inject

class StubIntroUtil @Inject constructor(): IntroUtil {
    override fun hasFirstRunIntroBeenShown() = true
    override fun markFirstRunIntroAsShown() {
        // no-op
    }
}
