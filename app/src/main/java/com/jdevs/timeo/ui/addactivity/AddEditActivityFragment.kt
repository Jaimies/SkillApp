package com.jdevs.timeo.ui.addactivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.jdevs.timeo.R
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.util.ActivitiesConstants.NAME_MAX_LENGTH
import com.jdevs.timeo.util.getCoroutineIoScope
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.requireMainActivity
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditActivityFragment : ActionBarFragment() {

    override val menuId = R.menu.addedit_fragment_menu
    private val args: AddEditActivityFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: AddEditActivityViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddactivityFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        requireMainActivity()?.supportActionBar?.apply {

            title =
                getString(if (args.isEdited) R.string.edit_activity else R.string.create_activity)
        }

        if (args.isEdited) {

            viewModel.setActivity(args.activity)
        }

        observeEvent(viewModel.hideKeyboard) {

            hideKeyboard()
        }

        observeEvent(viewModel.showDeleteDialog) {

            showDeleteDialog()
        }

        observeEvent(viewModel.saveActivity) {

            saveActivity(it!!)
        }

        return binding.root
    }

    private fun saveActivity(name: String) {

        if (!validateInput(name)) {

            return
        }

        if (args.isEdited) {

            val activity = args.activity!!.also {

                it.name = name
            }

            getCoroutineIoScope().launch {

                viewModel.saveActivity(activity)
            }

            val directions = AddEditActivityFragmentDirections
                .actionAddEditFragmentToActivityDetailFragment(activity = activity)

            findNavController().navigate(directions)
        } else {

            val activity = Activity(name = name)
            viewModel.addActivity(activity)

            findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
        }

        hideKeyboard()
    }

    private fun showDeleteDialog() {

        val dialog = AlertDialog.Builder(context!!)
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(getString(R.string.are_you_sure))
            .setMessage(getString(R.string.sure_delete_activity))
            .setPositiveButton(getString(R.string.yes)) { _, _ ->

                viewModel.deleteActivity(args.activity!!)

                Snackbar
                    .make(view!!, getString(R.string.activity_deleted), Snackbar.LENGTH_LONG).show()

                findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
            }
            .setNegativeButton(getString(R.string.no), null)

        dialog.show()
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

        return if (item.itemId == R.id.action_save) {

            viewModel.triggerSaveActivity()
            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
