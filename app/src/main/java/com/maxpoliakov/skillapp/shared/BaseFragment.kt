package com.maxpoliakov.skillapp.shared

import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.shared.analytics.logCurrentScreenToAnalytics

abstract class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        logCurrentScreenToAnalytics(this)
    }
}
