package com.maxpoliakov.skillapp.ui.addskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.R.id.action_addEditFragment_to_skillsFragment
import com.maxpoliakov.skillapp.R.menu.addskill_frag_menu
import com.maxpoliakov.skillapp.R.string.skill_deleted_message
import com.maxpoliakov.skillapp.R.string.cancel
import com.maxpoliakov.skillapp.R.string.create_skill
import com.maxpoliakov.skillapp.R.string.delete
import com.maxpoliakov.skillapp.R.string.delete_skill_message
import com.maxpoliakov.skillapp.R.string.delete_skill_title
import com.maxpoliakov.skillapp.R.string.edit_skill
import com.maxpoliakov.skillapp.databinding.AddskillFragBinding
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
class AddEditSkillFragment : ActionBarFragment(addskill_frag_menu) {
    private val viewModel by viewModels { viewModelFactory.create(args.skill) }

    @Inject
    lateinit var viewModelFactory: AddEditSkillViewModel.Factory
    private val args: AddEditSkillFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddskillFragBinding.inflate(inflater, container, false).also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val title = if (viewModel.skillExists) edit_skill else create_skill
        mainActivity.supportActionBar?.setTitle(title)

        observe(viewModel.navigateBack) { findNavController().popBackStack() }
        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(delete_skill_title)
            .setMessage(delete_skill_message)
            .setPositiveButton(delete) { _, _ -> deleteSkill() }
            .setNegativeButton(cancel, null)
            .show()
    }

    private fun deleteSkill() {
        viewModel.deleteSkill()
        snackbar(skill_deleted_message)
        navigate(action_addEditFragment_to_skillsFragment)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_save) {
            viewModel.saveSkill()
            return true
        }

        return false
    }
}
