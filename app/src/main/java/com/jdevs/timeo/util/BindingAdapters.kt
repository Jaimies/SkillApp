package com.jdevs.timeo.util

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.core.util.Consumer
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("hideIf")
fun hideIf(view: View, shouldHide: Boolean) {

    view.visibility = if (shouldHide) View.GONE else View.VISIBLE
}

@BindingAdapter("error")
fun setError(textInputLayout: TextInputLayout, error: String) {

    if (error.isEmpty()) {

        textInputLayout.isErrorEnabled = false
        return
    }

    textInputLayout.error = error
    textInputLayout.editText?.run {

        requestFocus()
        setSelection(length())

        doOnceAfterTextChanged {

            textInputLayout.isErrorEnabled = false
        }
    }
}

@BindingAdapter("onEnterPressed")
fun setOnEnterPressedListener(view: View, block: Runnable) {

    view.setOnKeyListener { _, _, _ ->

        block.run()
        true
    }
}

@BindingAdapter("items")
fun setSpinnerDropDownItems(spinner: Spinner, textArrayResId: Int) {

    spinner.adapter = ArrayAdapter.createFromResource(
        spinner.context, textArrayResId,
        android.R.layout.simple_list_item_1
    )
}

@BindingAdapter("onItemSelected")
fun setOnItemSelectedListener(spinner: Spinner, onItemSelected: Consumer<Int>) {

    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p2: Long) {

            onItemSelected.accept(position)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}

@BindingAdapter("selectedItem")
fun setSelectedItem(spinner: Spinner, position: Int) {

    spinner.setSelection(position)
}

private const val ANIMATION_DURATION = 400

@BindingAdapter("data")
fun setData(chart: LineChart, data: ChartData?) {

    if (data == null) {

        return
    }

    chart.data = chart.context.createLineData(data.items)
    chart.xAxis.valueFormatter = data.formatter
    chart.notifyDataSetChanged()
    chart.animateXY(ANIMATION_DURATION, ANIMATION_DURATION)
}

@BindingAdapter("android:onClick")
fun setOnClickListener(button: SignInButton, block: Runnable) {

    button.setOnClickListener { block.run() }
}
