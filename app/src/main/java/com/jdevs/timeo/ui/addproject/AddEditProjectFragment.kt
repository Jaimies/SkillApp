package com.jdevs.timeo.ui.addproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.AddprojectFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.fragment.mainActivity
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddEditProjectFragment : ActionBarFragment(R.menu.addactivity_frag_menu) {

    private val viewModel by viewModels { viewModelFactory.create(args.project) }

    @Inject
    lateinit var viewModelFactory: AddEditProjectViewModel.Factory

    private val args: AddEditProjectFragmentArgs by navArgs()
    private val isEdited get() = args.project != null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddprojectFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainActivity.supportActionBar?.setTitle(if (isEdited) R.string.edit_project else R.string.create_project)

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }
    }

    private fun saveProject() {

        val name = viewModel.name.value.orEmpty()
        val description = viewModel.description.value.orEmpty()

        if (!validateInput(name)) return

        if (isEdited) {
            val project = args.project!!.copy(name = name, description = description)
            viewModel.saveProject(project)
            findNavController().popBackStack()
        } else {
            viewModel.addProject(name, description)
            findNavController().navigate(R.id.action_addEditProjectFragment_to_projectsFragment)
        }
    }

    private fun showDeleteDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_project_title)
            .setMessage(R.string.delete_project_message)
            .setPositiveButton(R.string.delete) { _, _ ->
                viewModel.deleteProject(args.project!!)
                snackbar(R.string.project_deleted_message)
                findNavController().navigate(R.id.action_addEditProjectFragment_to_projectsFragment)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun validateInput(name: String): Boolean {

        when {
            name.isEmpty() -> setNameError(R.string.name_empty)
            name.length >= NAME_MAX_LENGTH -> setNameError(R.string.name_too_long)
            else -> return true
        }

        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.action_save) {
            saveProject()
            return true
        }

        return false
    }

    private fun setNameError(@StringRes resId: Int) = viewModel.setNameError(getString(resId))

    companion object {
        private const val NAME_MAX_LENGTH = 100
    }
}
