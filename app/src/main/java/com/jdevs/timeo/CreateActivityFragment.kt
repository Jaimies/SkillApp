package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.helpers.KeyboardHelper
import com.jdevs.timeo.model.ActionBarFragment
import kotlinx.android.synthetic.main.fragment_create_activity.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*

class CreateActivityFragment : ActionBarFragment() {

    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var titleEditText : EditText

    private lateinit var iconTextInputLayout: TextInputLayout
    private lateinit var iconEditText  : EditText

    private lateinit var spinningProgressBar: FrameLayout
    private lateinit var mainLayout : LinearLayout

    private lateinit var mFirebaseInstance: FirebaseFirestore
    private lateinit var mActivities : CollectionReference
    private lateinit var mAuth : FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_create_activity, container, false)

        titleEditText = view.titleEditText

        titleTextInputLayout = view.titleTextInputLayout


        iconEditText = view.iconEditText

        iconTextInputLayout = view.iconTextInputLayout


        spinningProgressBar = view.spinningProgressBar

        mainLayout = view.mainLayout


        mAuth = FirebaseAuth.getInstance()

        mFirebaseInstance = FirebaseFirestore.getInstance()

        mActivities = mFirebaseInstance.collection("users/${mAuth.uid}/activities")


        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_add_activity)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId) {
            R.id.doneAddingActivity -> {

                if(validateInput()) {

                    val title = titleEditText.text.toString()

                    val icon = iconEditText.text.toString()

                    val activity = TimeoActivity(title, icon)

                    findNavController().navigate(R.id.homeFragment)

                    hideKeyboard(getActivity())

                    mActivities.add(activity)
                        .addOnCompleteListener(getActivity() as android.app.Activity) { task ->

                            if (!task.isSuccessful) {

                                Log.w("Create activity", "Failed to save activity", task.exception)

                            }

                        }
                }

            }

            else -> {

                return super.onOptionsItemSelected(item)

            }
        }

        return true

    }


    private fun validateInput() : Boolean {

        if (titleEditText.text.isEmpty()) {

            titleTextInputLayout.error = "Title cannot be empty"

            return false

        }

        if (iconEditText.text.isEmpty()) {

            iconTextInputLayout.error = "Icon cannot be empty"

            return false

        }

        return true
    }


    companion object : KeyboardHelper()
}
