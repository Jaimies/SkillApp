package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.helpers.KeyboardHelper.Companion.hideKeyboard
import com.jdevs.timeo.models.ActionBarFragment
import kotlinx.android.synthetic.main.fragment_create_activity.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*

class CreateActivityFragment : ActionBarFragment() {

    private lateinit var titleTextInputLayout: TextInputLayout
    private lateinit var titleEditText : EditText

    private lateinit var iconTextInputLayout: TextInputLayout
    private lateinit var iconEditText  : EditText

    private lateinit var spinningProgressBar: FrameLayout
    private lateinit var mainLayout : LinearLayout

    private val mFirestore =  FirebaseFirestore.getInstance()

    private val args : CreateActivityFragmentArgs by navArgs()


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


        view.rootView.apply {

            setOnClickListener {

                hideKeyboard(activity)

            }

        }


        (requireActivity() as MainActivity).supportActionBar?.apply{

            title = if(args.editActivity) "Edit activity" else "Create activity"

        }

        if(args.editActivity) {


            titleEditText.setText(args.activityTitle)

            iconEditText.setText(args.activityIcon)

        }



        // Inflate the layout for this fragment
        return view
    }

    private fun validateInput() : Boolean {

        if (titleEditText.text.isEmpty()) {

            titleTextInputLayout.error = "Title cannot be empty"

            return false

        }

        if(titleEditText.text.length < 3) {

            titleTextInputLayout.error = "Title must be at least two characters long"

            return false

        }

        if(titleEditText.text.length >= 100) {

            titleTextInputLayout.error = "Title length must not exceed 100 characters"

            return false

        }

        if (iconEditText.text.isEmpty()) {

            iconTextInputLayout.error = "Icon cannot be empty"

            return false

        }

        if(iconEditText.text.length < 3) {

            iconTextInputLayout.error = "Icon must be at least three characters long"

            return false

        }

        if(iconEditText.text.length > 100) {

            iconTextInputLayout.error = "Icon length must not exceed 100 characters"

            return false

        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_create_activity)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.doneAddingActivity) {

            if(validateInput()) {

                val title = titleEditText.text.toString()

                val icon = iconEditText.text.toString()

                val timeoActivity = TimeoActivity(title, icon)

                if(args.editActivity) {

                    val activityRef = mFirestore.document("/users/${mUser.uid}/activities/${args.activityId}")

                    activityRef.update("title", title, "icon", icon).addOnFailureListener { firebaseException ->

                        Log.w("Create activity", "Failed to save activity", firebaseException)

                    }

                    val directions = CreateActivityFragmentDirections.actionReturnToActivityDetails(title, icon, args.activityId)

                    findNavController().navigate(directions)

                } else {

                    val activities = mFirestore.collection("users/${mUser.uid}/activities")

                    activities.add(timeoActivity)
                        .addOnFailureListener { firebaseException ->

                            Log.w("Create activity", "Failed to save activity", firebaseException)

                        }

                    findNavController().apply {

                        popBackStack(R.id.createActivityFragment, true)

                    }
                }

                hideKeyboard(activity)

            }

        } else {

            return super.onOptionsItemSelected(item)

        }

        return true

    }
}
