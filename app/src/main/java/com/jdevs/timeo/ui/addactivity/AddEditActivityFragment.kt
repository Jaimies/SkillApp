package com.jdevs.timeo.ui.addactivity

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
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.NAME_MAX_LENGTH
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.application
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.mainActivity
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.showSnackbar
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditActivityFragment : ActionBarFragment() {

    @Inject
    lateinit var viewModel: AddEditActivityViewModel

    override val menuId = R.menu.addactivity_frag_menu
    private val args: AddEditActivityFragmentArgs by navArgs()
    private var isEdited = false

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)

        isEdited = args.activity != null
        if (isEdited) viewModel.setActivity(args.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddactivityFragBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        mainActivity.supportActionBar?.setTitle(if (isEdited) R.string.edit_activity else R.string.create_activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observeEvent(viewModel.hideKeyboard) { hideKeyboard() }
        observeEvent(viewModel.showDeleteDialog) { showDeleteDialog() }
        observeEvent(viewModel.saveActivity) { saveActivity(it!!) }
    }

    private fun saveActivity(name: String) {

        if (!validateInput(name)) {

            return
        }

        if (isEdited) {

            val activity = args.activity!!.copy(name = name)

            application.ioScope.launch { viewModel.saveActivity(activity) }
            findNavController().popBackStack()
        } else {

            viewModel.addActivity(name)
            findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
        }
    }

    private fun showDeleteDialog() {

        AlertDialog.Builder(requireContext())
            .setIcon(android.R.drawable.ic_delete)
            .setTitle(R.string.are_you_sure)
            .setMessage(R.string.sure_delete_activity)
            .setPositiveButton(R.string.yes) { _, _ ->

                viewModel.deleteActivity(args.activity!!)
                showSnackbar(R.string.activity_deleted)
                findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
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

            viewModel.triggerSaveActivity()
            return true
        }

        return false
    }

    private fun setNameError(@StringRes resId: Int) = viewModel.setNameError(getString(resId))
}
