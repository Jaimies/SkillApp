package com.maxpoliakov.skillapp.ui.common

import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.util.analytics.logCurrentScreenToAnalytics

abstract class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        logCurrentScreenToAnalytics(this)
    }
}
