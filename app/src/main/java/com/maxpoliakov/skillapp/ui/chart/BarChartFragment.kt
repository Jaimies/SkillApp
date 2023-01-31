package com.maxpoliakov.skillapp.ui.chart

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.databinding.ViewDataBinding
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.jobs.MoveViewJob
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import kotlinx.parcelize.Parcelize

abstract class BarChartFragment<T : ViewDataBinding>(@MenuRes menuId: Int) : ActionBarFragment<T>(menuId) {
    abstract val T.chart: BarChart?

    override fun onSaveInstanceState(outState: Bundle) {
        val state = binding?.chart?.getChartState()
        outState.putParcelable(CHART_STATE, state)
    }

    @CallSuper
    override fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
        val state = savedInstanceState?.getChartState() ?: return
        binding.chart?.applyState(state)
    }

    private fun BarChart.applyState(state: ChartState) {
        viewPortHandler.zoom(state.scaleX, scaleY)
        moveViewToX(state.lowestVisibleX)
    }

    private fun Bundle.getChartState(): ChartState? = getParcelable(CHART_STATE)
    private fun BarChart.getChartState(): ChartState = ChartState(lowestVisibleX, scaleX)

    override fun onPause() {
        super.onPause()
        // Hack to prevent a memory leak, caused by a bug in MPAndroidChart
        MoveViewJob.getInstance(null, 0f, 0f, null, null)
    }

    @Parcelize
    data class ChartState(val lowestVisibleX: Float, val scaleX: Float) : Parcelable

    companion object {
        const val CHART_STATE = "CHART_STATE"
    }
}
