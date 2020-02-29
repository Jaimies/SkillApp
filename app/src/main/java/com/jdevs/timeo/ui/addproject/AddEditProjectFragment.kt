package com.jdevs.timeo.ui.addproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.AddprojectFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.NAME_MAX_LENGTH
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.mainActivity
import com.jdevs.timeo.util.observe
import com.jdevs.timeo.util.showSnackbar
import javax.inject.Inject

class AddEditProjectFragment : ActionBarFragment() {

    @Inject
    lateinit var viewModel: AddEditProjectViewModel

    private val args: AddEditProjectFragmentArgs by navArgs()
    override val menuId = R.menu.addproject_frag_menu

    private var isEdited = false

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)

        isEdited = args.project != null
        if (isEdited) viewModel.setProject(args.project)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddprojectFragBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        mainActivity.supportActionBar?.setTitle(if (isEdited) R.string.edit_project else R.string.create_project)

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }

        return binding.root
    }

    private fun saveProject() {

        val name = viewModel.name.value.orEmpty()
        val description = viewModel.name.value.orEmpty()

        if (!validateInput(name)) {
            return
        }

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

        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.sure_delete_project)
            .setPositiveButton(R.string.yes) { _, _ ->

                viewModel.deleteProject(args.project!!)
                showSnackbar(R.string.project_deleted)
                findNavController().navigate(R.id.action_addEditProjectFragment_to_projectsFragment)
            }
            .setNegativeButton(R.string.no, null)
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
}
