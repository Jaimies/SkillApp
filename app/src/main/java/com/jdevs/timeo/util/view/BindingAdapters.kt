@file:Suppress("MatchingDeclarationName")

package com.jdevs.timeo.util.view

import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.annotation.ArrayRes
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.BindingAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.common.SignInButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.common.TimeFormatter
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.HOURS_BREAKPOINT
import com.jdevs.timeo.util.charts.axisMaximum
import com.jdevs.timeo.util.getColorCompat

@BindingAdapter("visible")
fun View.visible(value: Boolean) {
    isVisible = value
}

@BindingAdapter("android:onClick")
fun SignInButton.setOnClickListener(block: Runnable) {

    setOnClickListener { block.run() }
}

@BindingAdapter("onEnterPressed")
fun EditText.setOnEnterPressedListener(block: Runnable) {

    setOnEditorActionListener { _, _, _ ->

        block.run()
        true
    }
}

@BindingAdapter("viewpager", "entries")
fun TabLayout.setupWithViewPager(viewPager: ViewPager2, @ArrayRes itemsResId: Int) {
    val items = context.resources.getStringArray(itemsResId)
    TabLayoutMediator(this, viewPager) { tab, position -> tab.text = items[position] }.attach()
}

@BindingAdapter("entries")
fun AutoCompleteTextView.setEntries(entries: List<Any>?) {

    if (entries == null) return
    setAdapter(ArrayAdapter(context, android.R.layout.simple_list_item_1, entries))
}

interface OnSelectedItemChangedListener {
    fun onChanged(newPosition: Int?)
}

@BindingAdapter("onSelectedItemChanged")
fun AutoCompleteTextView.setOnSelectedItemPositionChanged(onChanged: OnSelectedItemChangedListener) {
    setOnItemClickListener { _, _, position, _ -> onChanged.onChanged(position) }
    doAfterTextChanged { onChanged.onChanged(null) }
}

private const val VALUE_TEXT_SIZE = 12f
private const val LINE_WIDTH = 1.5f
private const val LABEL_COUNT = 5
private const val LABEL_COUNT_SMALL = 4

@BindingAdapter("data")
fun LineChart.setData(data: ChartData?) {

    if (data?.entries == null) {
        this.data = null
        return
    }

    val dataSet = LineDataSet(data.entries, "").apply {

        valueTextSize = VALUE_TEXT_SIZE
        valueFormatter = TimeFormatter()
        lineWidth = LINE_WIDTH
        color = context.getColorCompat(R.color.brown_800)
        setDrawCircles(false)
        isHighlightEnabled = false
    }

    this.data = LineData(dataSet)

    axisLeft.run {
        axisMaximum = data.entries.axisMaximum
        setLabelCount(if (axisMaximum <= HOURS_BREAKPOINT) LABEL_COUNT else LABEL_COUNT_SMALL, true)
    }

    xAxis.valueFormatter = data.formatter

    notifyDataSetChanged()
    invalidate()
}

@BindingAdapter("error")
fun TextInputLayout.setRemovableError(error: String?) {

    if (error.isNullOrEmpty()) {

        isErrorEnabled = false
        return
    }

    if (error == this.error) {
        return
    }

    this.error = error
    editText?.run {

        requestFocus()
        setSelection(length())
        doOnceAfterTextChanged { isErrorEnabled = false }
    }
}
