package com.maxpoliakov.skillapp.ui.common

import androidx.fragment.app.Fragment
import com.maxpoliakov.skillapp.util.analytics.setAsCurrentScreen

abstract class BaseFragment : Fragment() {
    override fun onResume() {
        super.onResume()
        this.setAsCurrentScreen()
    }
}
