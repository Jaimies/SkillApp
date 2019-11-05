package com.jdevs.timeo

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.helpers.KeyboardHelper.Companion.hideKeyboard
import com.jdevs.timeo.models.ActionBarFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_create_activity.view.*

class CreateActivityFragment : ActionBarFragment(),
    OnFailureListener {

    private lateinit var mActivityRef: DocumentReference

    private val mFirestore = FirebaseFirestore.getInstance()

    private val args: CreateActivityFragmentArgs by navArgs()

    private val mUser = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_create_activity, container, false)

        view.rootView.setOnClickListener {

            hideKeyboard(activity)
        }

        requireActivity().toolbar.apply {

            title = if (args.editActivity) "Edit activity" else "Create activity"
        }

        if (args.editActivity) {

            mActivityRef =
                mFirestore.document("/users/${mUser!!.uid}/activities/${args.activityId}")

            view.deleteButton.apply {

                visibility = View.VISIBLE

                setOnClickListener {

                    showDeleteDialog(view, context)
                }
            }

            view.titleEditText.setText(args.timeoActivity?.title)

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

        addOptionsMenu(menu, inflater, R.menu.action_bar_create_activity)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (mUser == null) {

            return false
        }

        if (item.itemId == R.id.doneAddingActivity) {

            if (validateInput()) {

                val title = view!!.titleEditText.text.toString()

                val icon = view!!.iconEditText.text.toString()

                if (args.editActivity) {

                    val timeoActivity = args.timeoActivity ?: return false

                    timeoActivity.title = title
                    timeoActivity.icon = icon

                    mActivityRef.update("title", title, "icon", icon)
                        .addOnFailureListener(this)

                    val records = mFirestore.collection("users/${mUser.uid}/records")

                    records.whereEqualTo("activityId", args.activityId).get()
                        .addOnSuccessListener { querySnapshot ->

                            if (querySnapshot != null && !querySnapshot.isEmpty) {

                                for (document in querySnapshot.documents) {

                                    document.reference.update("title", title)
                                }
                            }
                        }

                    val directions = CreateActivityFragmentDirections
                        .actionReturnToActivityDetails(timeoActivity, args.activityId ?: "")

                    findNavController().navigate(directions)
                } else {

                    val activities = mFirestore.collection("users/${mUser.uid}/activities")

                    val timeoActivity = TimeoActivity(title, icon)

                    activities.add(timeoActivity)
                        .addOnFailureListener(this)

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

    private fun showDeleteDialog(view: View, context: Context) {

        val dialog = AlertDialog.Builder(context)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Yes") { _: DialogInterface, _: Int ->

                mActivityRef
                    .delete()
                    .addOnFailureListener(this)

                Snackbar.make(view, "Activity deleted", Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_returnToHomeFragment)
            }
            .setNegativeButton("No", null)

        dialog.show()
    }
}
