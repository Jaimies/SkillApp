package com.theskillapp.skillapp.screenshots

import com.theskillapp.skillapp.ui.intro.IntroUtil
import javax.inject.Inject

class StubIntroUtil @Inject constructor(): IntroUtil {
    override fun hasFirstRunIntroBeenShown() = true
    override fun markFirstRunIntroAsShown() {
        // no-op
    }
}
