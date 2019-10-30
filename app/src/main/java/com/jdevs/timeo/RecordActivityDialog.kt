package com.jdevs.timeo

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.helpers.ScreenHelper.Companion.getDimensions
import kotlinx.android.synthetic.main.dialog_record_activity.*
import kotlin.math.roundToInt


class RecordActivityDialog(context: Context, private val activityName : String) : Dialog(context),
    View.OnFocusChangeListener {


    private val mFirebaseInstance =  FirebaseFirestore.getInstance()
    private val mAuth = FirebaseAuth.getInstance()

    private lateinit var mRecords : CollectionReference


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_record_activity)

        val dimensions = getDimensions(context)

        val width = (dimensions.widthPixels * 0.9).roundToInt()

        val height = (dimensions.heightPixels * 0.35).roundToInt()


        window?.apply {

            setLayout(width, height)

            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        }

        rootView.setOnClickListener {
            hoursEditText.clearFocus()
            minutesEditText.clearFocus()

        }


        hoursEditText.apply {

            onFocusChangeListener = this@RecordActivityDialog

        }

        minutesEditText.apply {

            onFocusChangeListener = this@RecordActivityDialog

        }


        addButton.setOnClickListener {

            submit()

        }

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        (v as EditText).apply {


            if(hasFocus || text.isEmpty()) {

                return

            }


            validateInput(v)

        }

    }



    private fun validateInput(editText: EditText) {

        editText.apply {

            val highLimit = if (id == R.id.hoursEditText) 23 else 59

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


        val hours= hoursEditText.text.toString().toIntOrNull() ?: 0

        val minutes = minutesEditText.text.toString().toIntOrNull() ?: return


        val time = hours * 60 + minutes


        val timeoRecord = TimeoRecord(activityName, time)

        mRecords = mFirebaseInstance.collection("/users/${mAuth.currentUser!!.uid}/records")

        mRecords.add(timeoRecord).addOnFailureListener { firebaseException ->

            Log.w("Create activity", "Failed to save activity", firebaseException)

        }

        dismiss()

    }
}