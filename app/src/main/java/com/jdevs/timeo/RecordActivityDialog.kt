package com.jdevs.timeo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.util.getMins
import com.jdevs.timeo.util.getScreenDimensions
import kotlinx.android.synthetic.main.dialog_record_activity.add_button
import kotlinx.android.synthetic.main.dialog_record_activity.hours_edit_text
import kotlinx.android.synthetic.main.dialog_record_activity.minutes_edit_text
import kotlinx.android.synthetic.main.dialog_record_activity.rootView
import kotlin.math.roundToInt

class RecordActivityDialog(
    context: Context,
    private val index: Int,
    private val createRecord: (Int, Long) -> Unit = { _, _ -> }
) : Dialog(context),
    View.OnFocusChangeListener,
    OnFailureListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_record_activity)

        val dimensions = getScreenDimensions(context)

        val width = (dimensions.widthPixels * DIALOG_WIDTH).roundToInt()
        val height = (dimensions.heightPixels * DIALOG_HEIGHT).roundToInt()

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

        add_button.setOnClickListener { addRecord() }
    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        (v as EditText).apply {

            if (hasFocus || text.isEmpty()) {

                return
            }

            validateInput(v)
        }
    }

    private fun validateInput(editText: EditText) {

        editText.apply {

            val highLimit = if (id == R.id.hours_edit_text) MAX_HOURS else MAX_MINS
            val lowLimit = if (id == R.id.hours_edit_text) 0 else 5

            val value = text.toString().toIntOrNull() ?: return

            if (value > highLimit) {

                setText(highLimit.toString())
                return
            }

            if (value < lowLimit) {

                setText(lowLimit.toString())
            }
        }
    }

    private fun addRecord() {

        validateInput(hours_edit_text)
        validateInput(minutes_edit_text)

        val hours = hours_edit_text.text.toString().toLongOrNull() ?: 0
        val minutes = minutes_edit_text.text.toString().toLongOrNull() ?: return

        val time = Pair(hours, minutes).getMins()

        createRecord(index, time)

        dismiss()
    }

    override fun onFailure(firebaseException: Exception) {

        Log.w("Create record", "Failed to create activity", firebaseException)

        Snackbar.make(rootView, "Failed to record activity", Snackbar.LENGTH_SHORT).show()
    }

    companion object {

        const val DIALOG_WIDTH = 0.9
        const val DIALOG_HEIGHT = 0.35

        const val MAX_MINS = 59
        const val MAX_HOURS = 23
    }
}
