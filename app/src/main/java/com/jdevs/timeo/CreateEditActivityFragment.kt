package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.FragmentCreateEditActivityBinding
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.repositories.FirestoreActivitiesListRepository
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.viewmodels.CreateEditActivityViewModel
import kotlinx.android.synthetic.main.activity_main.toolbar

class CreateEditActivityFragment : ActionBarFragment(),
    CreateEditActivityViewModel.Navigator {
    private val args: CreateEditActivityFragmentArgs by navArgs()

    override val menuId = R.menu.action_bar_create_activity

    private val firestoreActivitiesListRepository =
        FirestoreActivitiesListRepository()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CreateEditActivityViewModel::class.java).also {

            it.navigator = this

            if (args.editActivity) {

                it.setActivity(args.timeoActivity)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentCreateEditActivityBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this
        }


        requireActivity().toolbar.apply {

            title = if (args.editActivity) "Edit activity" else "Create activity"
        }

        return binding.root
    }

    override fun saveActivity(name: String, icon: String) {

        if (!validateInput(name, icon)) {

            return
        }

        if (args.editActivity) {

            val timeoActivity = args.timeoActivity ?: return

            timeoActivity.also {

                it.name = name
                it.icon = icon
            }

            firestoreActivitiesListRepository.updateActivity(
                timeoActivity,
                args.activityId ?: return
            )

            val directions = CreateEditActivityFragmentDirections
                .actionReturnToActivityDetails(timeoActivity, args.activityId ?: "")

            findNavController().navigate(directions)
        } else {

            val timeoActivity = TimeoActivity(name, icon)

            firestoreActivitiesListRepository.createActivity(timeoActivity)

            findNavController().navigate(R.id.action_returnToHomeFragment)
        }

        hideKeyboard()
    }

    override fun showDeleteDialog() {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle("Are you sure?")
            .setMessage("Are you sure you want to delete this activity?")
            .setPositiveButton("Yes") { _, _ ->

                firestoreActivitiesListRepository.deleteActivity(
                    args.activityId ?: return@setPositiveButton
                )

                Snackbar.make(view!!, "Activity deleted", Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_returnToHomeFragment)
            }
            .setNegativeButton("No", null)

        dialog.show()
    }

    override fun hideKeyboard() {

        hideKeyboard(activity)
    }

    private fun validateInput(name: String, icon: String): Boolean {

        when {
            name.isEmpty() -> viewModel.setNameError("Title cannot be empty")
            name.length >= 100 -> viewModel.setNameError("Title length must not exceed 100 characters")
            icon.isEmpty() -> viewModel.setIconError("Icon cannot be empty")
            icon.length >= 100 -> viewModel.setIconError("Icon length must not exceed 100 characters")

            else -> {
                return true
            }
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save) {

            viewModel.triggerSaveActivity()
        } else {

            return super.onOptionsItemSelected(item)
        }

        return true
    }
}
