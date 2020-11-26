package com.maxpoliakov.skillapp.ui.addactivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.R.id.action_addEditFragment_to_activitiesFragment
import com.maxpoliakov.skillapp.R.menu.addactivity_frag_menu
import com.maxpoliakov.skillapp.R.string.activity_deleted_message
import com.maxpoliakov.skillapp.R.string.cancel
import com.maxpoliakov.skillapp.R.string.create_activity
import com.maxpoliakov.skillapp.R.string.delete
import com.maxpoliakov.skillapp.R.string.delete_activity_message
import com.maxpoliakov.skillapp.R.string.delete_activity_title
import com.maxpoliakov.skillapp.R.string.edit_activity
import com.maxpoliakov.skillapp.databinding.AddactivityFragBinding
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.util.fragment.mainActivity
import com.maxpoliakov.skillapp.util.fragment.navigate
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.snackbar
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
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
