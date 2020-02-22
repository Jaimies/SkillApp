package com.jdevs.timeo.util

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.core.util.Consumer
import androidx.databinding.BindingAdapter
import com.github.mikephil.charting.charts.LineChart
import com.google.android.gms.common.SignInButton
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("hideIf")
fun View.hideIf(shouldHide: Boolean) {

    visibility = if (shouldHide) View.GONE else View.VISIBLE
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

@BindingAdapter("onEnterPressed")
fun EditText.setOnEnterPressedListener(block: Runnable) {

    setOnEditorActionListener { _, _, _ ->

        block.run()
        true
    }
}

@BindingAdapter("items")
fun Spinner.setDropDownItems(textArrayResId: Int) {

    adapter = ArrayAdapter.createFromResource(
        context, textArrayResId,
        android.R.layout.simple_list_item_1
    )
}

@BindingAdapter("onItemSelected")
fun Spinner.setOnItemSelectedListener(onItemSelected: Consumer<Int>) {

    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p2: Long) {

            onItemSelected.accept(position)
        }

        override fun onNothingSelected(p0: AdapterView<*>?) {}
    }
}

@BindingAdapter("selectedItem")
fun Spinner.setSelectedItem(position: Int) {

    setSelection(position)
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

@BindingAdapter("android:onClick")
fun SignInButton.setOnClickListener(block: Runnable) {

    setOnClickListener { block.run() }
}
