package com.maxpoliakov.skillapp.util.ui

import android.os.SystemClock
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.maxpoliakov.skillapp.model.BarChartData
import com.maxpoliakov.skillapp.model.PieChartData
import com.maxpoliakov.skillapp.util.charts.TheBarChart
import com.maxpoliakov.skillapp.util.charts.ThePieChart

@BindingAdapter("visible")
fun View.isVisible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("layout_sideMargin")
fun setSideMargins(view: View, dimen: Float) {
    val layoutParams = view.layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.marginStart = dimen.toInt()
    layoutParams.marginEnd = dimen.toInt()
    view.layoutParams = layoutParams
}

@BindingAdapter("layout_marginBottom")
fun View.setBottomMargin(dimen: Float) {
    val layoutParams = layoutParams as ViewGroup.MarginLayoutParams
    layoutParams.bottomMargin = dimen.toInt()
    this.layoutParams = layoutParams
}

@BindingAdapter("format")
fun Chronometer.setTimeFormat(format: String) {
    setOnChronometerTickListener { chronometer ->
        val time = SystemClock.elapsedRealtime() - chronometer.base
        chronometer.post {
            // todo consider a different method for formatting
            chronometer.text = DateFormat.format(format, time)
        }
    }
}

@BindingAdapter("backgroundTintAttr")
fun View.setBackgroundTintAttr(attrValue: Int) {
    setBackgroundColor(context.getColorAttributeValue(attrValue))
}

@BindingAdapter("textColorAttr")
fun MaterialButton.setTextColorAttr(attrValue: Int) {
    setTextColor(context.getColorAttributeValue(attrValue))
}


@BindingAdapter(
    "layout_constraint_startSide",
    "layout_constraint_toEndId",
    "layout_constraint_endSide",
)
fun View.setConditionalConstraint(
    startSide: Int, endId: Int, endSide: Int,
) {
    val constraintLayout = (parent as? ConstraintLayout) ?: return
    with(ConstraintSet()) {
        clone(constraintLayout)
        connect(id, startSide, endId, endSide)
        applyTo(constraintLayout)
    }
}

@BindingAdapter("data")
fun TheBarChart.setData(data: BarChartData?) {
    update(data)
}

@BindingAdapter("data")
fun ThePieChart.setData(data: PieChartData?) {
    data?.let(this::update)
}
