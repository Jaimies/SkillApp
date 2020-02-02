package com.jdevs.timeo.ui.activities

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.EditText
import com.jdevs.timeo.R
import com.jdevs.timeo.util.RecordsConstants.RECORD_MIN_TIME
import com.jdevs.timeo.util.getScreenDimensions
import com.jdevs.timeo.util.time.DAY_HOURS
import com.jdevs.timeo.util.time.HOUR_MINUTES
import com.jdevs.timeo.util.time.getMins
import kotlinx.android.synthetic.main.record_dialog.add_button
import kotlinx.android.synthetic.main.record_dialog.hours_edit_text
import kotlinx.android.synthetic.main.record_dialog.minutes_edit_text
import kotlinx.android.synthetic.main.record_dialog.rootView
import kotlin.math.roundToInt

class RecordDialog(
    context: Context,
    private val createRecord: (Long) -> Unit = {}
) : Dialog(context), View.OnFocusChangeListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.record_dialog)

        val dimensions = context.getScreenDimensions()
        val width = (dimensions.widthPixels * WIDTH).roundToInt()
        val height = (dimensions.heightPixels * HEIGHT).roundToInt()

        window?.apply {

            setLayout(width, height)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        rootView.setOnClickListener {
            hours_edit_text.clearFocus()
            minutes_edit_text.clearFocus()
        }

        hours_edit_text.onFocusChangeListener = this
        minutes_edit_text.onFocusChangeListener = this

        add_button.setOnClickListener { createRecord() }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        v as EditText

        if (v.hasFocus() || v.text.isEmpty()) return
        v.validateInput()
    }

    private fun EditText.validateInput() {

        val isHours = id == R.id.hours_edit_text
        val maxValue = if (isHours) DAY_HOURS - 1 else HOUR_MINUTES - 1
        val minValue = if (isHours) 0 else RECORD_MIN_TIME
        val value = text.toString().toIntOrNull() ?: return

        setText(value.coerceIn(minValue, maxValue).toString())
    }

    private fun createRecord() {

        hours_edit_text.validateInput()
        minutes_edit_text.validateInput()

        val hours = hours_edit_text.text.toString().toLongOrNull() ?: 0
        val minutes = minutes_edit_text.text.toString().toLongOrNull() ?: return

        val time = getMins(hours, minutes)
        createRecord(time)
        dismiss()
    }

    companion object {
        const val WIDTH = 0.9
        const val HEIGHT = 0.35
    }
}
