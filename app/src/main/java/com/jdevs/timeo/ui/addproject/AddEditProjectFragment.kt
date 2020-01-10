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
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.AddprojectFragBinding
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.util.ActivitiesConstants.NAME_MAX_LENGTH
import com.jdevs.timeo.util.getCoroutineIoScope
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.requireMainActivity
import com.jdevs.timeo.util.showSnackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditProjectFragment : ActionBarFragment() {

    private var isEdited = false
    private val args: AddEditProjectFragmentArgs by navArgs()
    override val menuId = R.menu.addedit_project_fragment_menu

    @Inject
    lateinit var viewModel: AddEditProjectViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        isEdited = args.project != null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddprojectFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        requireMainActivity()?.supportActionBar
            ?.setTitle(if (isEdited) R.string.edit_project else R.string.create_project)

        if (isEdited) {

            viewModel.setProject(args.project)
        }

        observeEvent(viewModel.hideKeyboard) { hideKeyboard() }
        observeEvent(viewModel.showDeleteDialog) { showDeleteDialog() }
        observeEvent(viewModel.saveProject) { saveProject(it!!) }

        return binding.root
    }

    private fun saveProject(name: String) {

        if (!validateInput(name)) {
            return
        }

        if (isEdited) {

            val project = args.project!!.copy(name = name)
            getCoroutineIoScope().launch { viewModel.saveProject(project) }
            findNavController().popBackStack()
        } else {

            val project = Project(name = name)
            viewModel.addProject(project)
            findNavController().navigate(R.id.action_addEditProjectFragment_to_projectsFragment)
        }

        hideKeyboard()
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

        return if (item.itemId == R.id.action_save) {

            viewModel.triggerSaveProject()
            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }

    private fun setNameError(@StringRes resId: Int) = viewModel.setNameError(getString(resId))
}
