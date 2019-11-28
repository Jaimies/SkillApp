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
import com.jdevs.timeo.repository.FirestoreActivitiesListRepository
import com.jdevs.timeo.util.ACTIVITY_NAME_MAX_LENGTH
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.viewmodel.CreateEditActivityViewModel
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

            title =
                getString(if (args.editActivity) R.string.edit_activity else R.string.create_activity)
        }

        return binding.root
    }

    override fun saveActivity(name: String, icon: String) {

        if (!validateInput(name, icon)) {

            return
        }

        if (args.editActivity) {

            val timeoActivity = args.timeoActivity!!

            timeoActivity.also {

                it.name = name
                it.icon = icon
            }

            firestoreActivitiesListRepository.updateActivity(timeoActivity, args.activityId!!)

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
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_activity))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                firestoreActivitiesListRepository.deleteActivity(
                    args.activityId ?: return@setPositiveButton
                )

                Snackbar.make(view!!, getString(R.string.activity_deleted), Snackbar.LENGTH_LONG)
                    .show()

                findNavController().navigate(R.id.action_returnToHomeFragment)
            }
            .setNegativeButton(getString(R.string.no), null)

        dialog.show()
    }

    override fun hideKeyboard() {

        hideKeyboard(activity)
    }

    private fun validateInput(name: String, icon: String): Boolean {

        when {
            name.isEmpty() -> viewModel.setNameError(getString(R.string.name_empty))
            icon.isEmpty() -> viewModel.setIconError(getString(R.string.icon_empty))

            name.length >= ACTIVITY_NAME_MAX_LENGTH -> {
                viewModel.setNameError(getString(R.string.name_too_long))
            }

            icon.length >= ACTIVITY_NAME_MAX_LENGTH -> {
                viewModel.setIconError(getString(R.string.icon_too_long))
            }

            else -> return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.save) {

            viewModel.triggerActivitySave()
        } else {

            return super.onOptionsItemSelected(item)
        }

        return true
    }
}
