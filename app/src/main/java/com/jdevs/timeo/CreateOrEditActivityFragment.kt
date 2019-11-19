package com.jdevs.timeo

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.repositories.FirestoreActivitiesListRepository
import com.jdevs.timeo.util.Keyboard.Companion.hide
import com.jdevs.timeo.util.TAG
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.fragment_create_or_edit_activity.view.deleteButton
import kotlinx.android.synthetic.main.fragment_create_or_edit_activity.view.iconEditText
import kotlinx.android.synthetic.main.fragment_create_or_edit_activity.view.iconTextInputLayout
import kotlinx.android.synthetic.main.fragment_create_or_edit_activity.view.titleEditText
import kotlinx.android.synthetic.main.fragment_create_or_edit_activity.view.titleTextInputLayout

class CreateOrEditActivityFragment : ActionBarFragment(),
    OnFailureListener {
    private val args: CreateOrEditActivityFragmentArgs by navArgs()

    private val firestoreActivitiesListRepository =
        FirestoreActivitiesListRepository()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_or_edit_activity, container, false)

        view.rootView.setOnClickListener {

            hide(activity)
        }

        requireActivity().toolbar.apply {

            title = if (args.editActivity) "Edit activity" else "Create activity"
        }

        if (args.editActivity) {

            view.deleteButton.apply {

                visibility = View.VISIBLE

                setOnClickListener {

                    showDeleteDialog(view, context)
                }
            }

            view.titleEditText.setText(args.timeoActivity?.name)

            view.iconEditText.setText(args.timeoActivity?.icon)
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onFailure(firebaseException: Exception) {

        Log.w(
            TAG,
            "Failed to save data to Firestore",
            firebaseException
        )
    }

    private fun validateInput(): Boolean {

        val titleText = view!!.titleEditText.text ?: return false
        val iconText = view!!.iconEditText.text ?: return false

        view!!.titleTextInputLayout.apply {

            if (titleText.isEmpty()) {

                error = "Title cannot be empty"

                return false
            }

            if (titleText.length < 3) {

                error = "Title must be at least two characters long"

                return false
            }

            if (titleText.length >= 100) {

                error = "Title length must not exceed 100 characters"

                return false
            }
        }

        view!!.iconTextInputLayout.apply {

            if (iconText.isEmpty()) {

                error = "Icon cannot be empty"

                return false
            }

            if (iconText.length < 3) {

                error = "Icon must be at least three characters long"

                return false
            }

            if (iconText.length > 100) {

                error = "Icon length must not exceed 100 characters"

                return false
            }
        }

        return true
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        createOptionsMenu(menu, inflater, R.menu.action_bar_create_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.done) {

            if (validateInput()) {

                createActivity()
            }
        } else {

            return super.onOptionsItemSelected(item)
        }

        return true
    }

    private fun createActivity() {

        val title = view!!.titleEditText.text.toString()

        val icon = view!!.iconEditText.text.toString()

        if (args.editActivity) {

            val timeoActivity = args.timeoActivity ?: return

            timeoActivity.apply {

                this.name = title
                this.icon = icon
            }

            firestoreActivitiesListRepository.updateActivity(
                timeoActivity,
                args.activityId ?: return
            )

            val directions = CreateOrEditActivityFragmentDirections
                .actionReturnToActivityDetails(timeoActivity, args.activityId ?: "")

            findNavController().navigate(directions)
        } else {

            val timeoActivity = TimeoActivity(title, icon)

            firestoreActivitiesListRepository.createActivity(timeoActivity)

            findNavController().navigate(R.id.action_returnToHomeFragment)
        }

        hide(activity)
    }

    private fun showDeleteDialog(view: View, context: Context) {

        val dialog = AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->

                firestoreActivitiesListRepository.deleteActivity(
                    args.activityId ?: return@setPositiveButton
                )

                Snackbar.make(view, "Activity deleted", Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_returnToHomeFragment)
            }
            .setNegativeButton("No", null)

        dialog.show()
    }
}
