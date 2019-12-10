package com.jdevs.timeo.ui.activities

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
import com.jdevs.timeo.R
import com.jdevs.timeo.api.repository.firestore.ActivitiesRepository
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.AddtaskFragBinding
import com.jdevs.timeo.ui.activities.viewmodel.CreateEditActivityViewModel
import com.jdevs.timeo.util.ActivitiesConstants.NAME_MAX_LENGTH
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.requireMainActivity

class AddEditActivityFragment : ActionBarFragment(),
    CreateEditActivityViewModel.Navigator {

    override val menuId = R.menu.addedit_fragment_menu
    private val args: AddEditActivityFragmentArgs by navArgs()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(CreateEditActivityViewModel::class.java).also {

            it.navigator = this

            if (args.isEdited) {

                it.setActivity(args.activity)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = AddtaskFragBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this
        }

        requireMainActivity().supportActionBar?.apply {

            title =
                getString(if (args.isEdited) R.string.edit_activity else R.string.create_activity)
        }

        return binding.root
    }

    override fun saveActivity(name: String) {

        if (!validateInput(name)) {

            return
        }

        if (args.isEdited) {

            val activity = args.activity!!.also {

                it.name = name
            }

            ActivitiesRepository.updateActivity(activity, args.id!!)

            val directions =
                AddEditActivityFragmentDirections.actionAddEditFragmentToActivityDetailFragment(
                    activity = activity,
                    id = args.id ?: ""
                )

            findNavController().navigate(directions)
        } else {

            val activity = TimeoActivity(name)

            ActivitiesRepository.createActivity(activity)

            findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
        }

        hideKeyboard()
    }

    override fun showDeleteDialog() {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_activity))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                ActivitiesRepository.deleteActivity(args.id ?: return@setPositiveButton)

                Snackbar
                    .make(view!!, getString(R.string.activity_deleted), Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
            }
            .setNegativeButton(getString(R.string.no), null)

        dialog.show()
    }

    override fun hideKeyboard() {

        hideKeyboard(activity)
    }

    private fun validateInput(name: String): Boolean {

        when {
            name.isEmpty() -> viewModel.setNameError(getString(R.string.name_empty))
            name.length >= NAME_MAX_LENGTH -> viewModel.setNameError(getString(R.string.name_too_long))

            else -> return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_save) {

            viewModel.triggerSaveActivity()
        } else {

            return super.onOptionsItemSelected(item)
        }

        return true
    }
}
