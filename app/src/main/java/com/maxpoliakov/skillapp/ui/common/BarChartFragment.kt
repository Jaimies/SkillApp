package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import com.github.mikephil.charting.charts.BarChart

abstract class BarChartFragment(@MenuRes menuId: Int) : ActionBarFragment(menuId) {
    abstract val chart: BarChart

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat(SCROLL_POSITION, chart.lowestVisibleX)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val position = savedInstanceState.getFloat(SCROLL_POSITION)
            chart.moveViewToX(position)
        }
    }

    companion object {
        const val SCROLL_POSITION = "BarChartFragment.chartScrollPosition"
    }
}
