package com.maxpoliakov.skillapp.ui.chart

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.jobs.MoveViewJob
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment

abstract class BarChartFragment<T: ViewDataBinding>(@MenuRes menuId: Int) : ActionBarFragment<T>(menuId) {
    abstract val T.chart: BarChart?

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(SCROLL_POSITION, binding?.chart?.lowestVisibleX ?: return)
    }

    @CallSuper
    override fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
        val position = savedInstanceState?.getFloat(SCROLL_POSITION) ?: return
        binding.chart?.moveViewToX(position)
    }

    override fun onPause() {
        super.onPause()
        // Hack to prevent a memory leak, caused by a bug in MPAndroidChart
        MoveViewJob.getInstance(null, 0f, 0f, null, null)
    }

    companion object {
        const val SCROLL_POSITION = "BarChartFragment.chartScrollPosition"
    }
}
