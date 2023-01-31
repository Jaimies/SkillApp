package com.maxpoliakov.skillapp.ui.chart

import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import com.github.mikephil.charting.jobs.MoveViewJob
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment

abstract class BarChartFragment<T : ViewDataBinding>(@MenuRes menuId: Int) : ActionBarFragment<T>(menuId) {
    override fun onPause() {
        super.onPause()
        // Hack to prevent a memory leak, caused by a bug in MPAndroidChart
        MoveViewJob.getInstance(null, 0f, 0f, null, null)
    }
}
