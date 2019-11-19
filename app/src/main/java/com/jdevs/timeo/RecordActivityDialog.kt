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
import com.jdevs.timeo.util.Screen
import com.jdevs.timeo.util.Time
import kotlinx.android.synthetic.main.dialog_record_activity.addButton
import kotlinx.android.synthetic.main.dialog_record_activity.hoursEditText
import kotlinx.android.synthetic.main.dialog_record_activity.minutesEditText
import kotlinx.android.synthetic.main.dialog_record_activity.rootView
import kotlin.math.roundToInt

class RecordActivityDialog(
    context: Context,
    private val index: Int,
    private val createRecord: (Int, Int) -> Unit = { _, _ -> }
) : Dialog(context),
    View.OnFocusChangeListener,
    OnFailureListener {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_record_activity)

        val dimensions = Screen.getDimensions(context)

        val width = (dimensions.widthPixels * DIALOG_WIDTH).roundToInt()

        val height = (dimensions.heightPixels * DIALOG_HEIGHT).roundToInt()

        window?.apply {

            setLayout(width, height)

            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        rootView.setOnClickListener {
            hoursEditText.clearFocus()
            minutesEditText.clearFocus()
        }

        hoursEditText.onFocusChangeListener = this

        minutesEditText.onFocusChangeListener = this

        addButton.setOnClickListener { submit() }
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

            val highLimit = if (id == R.id.hoursEditText) MAX_HOURS else MAX_MINS

            val lowLimit = if (id == R.id.hoursEditText) 0 else 5

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

    private fun submit() {

        validateInput(hoursEditText)
        validateInput(minutesEditText)

        val hours = hoursEditText.text.toString().toIntOrNull() ?: 0
        val minutes = minutesEditText.text.toString().toIntOrNull() ?: return

        val time = Time.timeToMins(Pair(hours, minutes))

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
