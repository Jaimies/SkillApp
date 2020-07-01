package com.jdevs.timeo.ui.addactivity

import android.content.Context
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
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.application
import com.jdevs.timeo.util.fragment.mainActivity
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.lifecycle.viewModels
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditActivityFragment : ActionBarFragment() {

    private val viewModel by viewModels { viewModelFactory.create(args.activity) }

    @Inject
    lateinit var viewModelFactory: AddEditActivityViewModel.Factory

    override val menuId = R.menu.addactivity_frag_menu
    private val args: AddEditActivityFragmentArgs by navArgs()
    private val isEdited get() = args.activity != null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddactivityFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mainActivity.supportActionBar?.setTitle(if (isEdited) R.string.edit_activity else R.string.create_activity)

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }
    }

    private fun saveActivity() {

        val name = viewModel.name.value.orEmpty()

        if (!validateInput(name)) {
            return
        }

        val index = viewModel.parentActivityIndex

        if (index == null) {

            viewModel.parentActivityError.value = getString(R.string.invalid_activity_error)
            return
        }

        if (args.activity != null) {

            application.ioScope.launch { viewModel.saveActivity(args.activity!!, index) }
            findNavController().popBackStack()
        } else {

            viewModel.addActivity(index)
            findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
        }
    }

    private fun showDeleteDialog() {

        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_activity_title)
            .setMessage(R.string.delete_activity_message)
            .setPositiveButton(R.string.delete) { _, _ ->

                viewModel.deleteActivity(args.activity!!)
                snackbar(R.string.activity_deleted_message)
                findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
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

            saveActivity()
            return true
        }

        return false
    }

    private fun setNameError(@StringRes resId: Int) {
        viewModel.nameError.value = getString(resId)
    }

    companion object {
        private const val NAME_MAX_LENGTH = 100
    }
}
