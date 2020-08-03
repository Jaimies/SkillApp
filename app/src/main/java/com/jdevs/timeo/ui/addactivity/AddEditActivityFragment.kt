package com.jdevs.timeo.ui.addactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jdevs.timeo.R
import com.jdevs.timeo.R.id.action_addEditFragment_to_activitiesFragment
import com.jdevs.timeo.R.menu.addactivity_frag_menu
import com.jdevs.timeo.R.string.activity_deleted_message
import com.jdevs.timeo.R.string.cancel
import com.jdevs.timeo.R.string.create_activity
import com.jdevs.timeo.R.string.delete
import com.jdevs.timeo.R.string.delete_activity_message
import com.jdevs.timeo.R.string.delete_activity_title
import com.jdevs.timeo.R.string.edit_activity
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.fragment.mainActivity
import com.jdevs.timeo.util.fragment.navigate
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddEditActivityFragment : ActionBarFragment(addactivity_frag_menu) {
    private val viewModel by viewModels { viewModelFactory.create(args.activity) }

    @Inject
    lateinit var viewModelFactory: AddEditActivityViewModel.Factory
    private val args: AddEditActivityFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding =
            AddactivityFragBinding.inflate(inflater, container, false).also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val title =
            if (viewModel.activityExists) edit_activity else create_activity
        mainActivity.supportActionBar?.setTitle(title)

        observe(viewModel.navigateBack) { findNavController().popBackStack() }
        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(delete_activity_title)
            .setMessage(delete_activity_message)
            .setPositiveButton(delete) { _, _ -> deleteActivity() }
            .setNegativeButton(cancel, null)
            .show()
    }

    private fun deleteActivity() {
        viewModel.deleteActivity()
        snackbar(activity_deleted_message)
        navigate(action_addEditFragment_to_activitiesFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            viewModel.saveActivity()
            return true
        }

        return false
    }
}
