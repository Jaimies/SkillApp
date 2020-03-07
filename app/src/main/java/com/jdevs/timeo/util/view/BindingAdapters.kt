package com.jdevs.timeo.util.view

import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.createLineData

@BindingAdapter("hideIf")
fun View.hideIf(shouldHide: Boolean) {

    visibility = if (shouldHide) View.GONE else View.VISIBLE
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

@BindingAdapter("android:entries")
fun Spinner.setEntries(@ArrayRes resId: Int) {
    adapter = ArrayAdapter.createFromResource(context, resId, android.R.layout.simple_list_item_1)
}

private const val ANIMATION_DURATION = 400

@BindingAdapter("data")
fun LineChart.setData(data: ChartData?) {

    if (data == null) {

        return
    }

    this.data = context.createLineData(data.items)
    xAxis.valueFormatter = data.formatter
    notifyDataSetChanged()
    animateXY(ANIMATION_DURATION, ANIMATION_DURATION)
}

@BindingAdapter("error")
fun TextInputLayout.setRemovableError(error: String) {

    if (error.isEmpty()) {

        isErrorEnabled = false
        return
    }

    this.error = error
    editText?.run {

        requestFocus()
        setSelection(length())

        doOnceAfterTextChanged {

            isErrorEnabled = false
        }
    }
}
