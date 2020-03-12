package com.jdevs.timeo.util.view

import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout
import com.jdevs.timeo.util.charts.ChartData
import com.jdevs.timeo.util.charts.createLineData

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

@BindingAdapter("android:entries")
fun Spinner.setEntries(@ArrayRes resId: Int) {
    adapter = ArrayAdapter.createFromResource(context, resId, android.R.layout.simple_list_item_1)
}

@BindingAdapter("data")
fun LineChart.setData(data: ChartData?) {

    if (data?.items == null) {

        return
    }

    this.data = context.createLineData(data.items)
    xAxis.valueFormatter = data.formatter
    notifyDataSetChanged()
    invalidate()
}

@BindingAdapter("error")
fun TextInputLayout.setRemovableError(error: String?) {

    if (error == null || error.isEmpty()) {

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

        doOnceAfterTextChanged {

            isErrorEnabled = false
        }
    }
}
