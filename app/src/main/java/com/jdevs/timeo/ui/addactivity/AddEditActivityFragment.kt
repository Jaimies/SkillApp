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
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.util.ActivitiesConstants.NAME_MAX_LENGTH
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.getCoroutineIoScope
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.requireMainActivity
import com.jdevs.timeo.util.extensions.showSnackbar
import com.jdevs.timeo.util.hideKeyboard
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditActivityFragment : ActionBarFragment() {

    override val menuId = R.menu.addedit_activity_fragment_menu
    private val args: AddEditActivityFragmentArgs by navArgs()
    private var isEdited = false

    @Inject
    lateinit var viewModel: AddEditActivityViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        isEdited = args.activity != null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = AddactivityFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
        }

        requireMainActivity().supportActionBar
            ?.setTitle(if (isEdited) R.string.edit_activity else R.string.create_activity)

        if (isEdited) {

            viewModel.setActivity(args.activity)
        }

        observeEvent(viewModel.hideKeyboard) { hideKeyboard() }
        observeEvent(viewModel.showDeleteDialog) { showDeleteDialog() }
        observeEvent(viewModel.saveActivity) { saveActivity(it!!) }

        return binding.root
    }

    private fun saveActivity(name: String) {

        if (!validateInput(name)) {

            return
        }

        if (isEdited) {

            val activity = args.activity!!.copy(name = name)

            getCoroutineIoScope().launch { viewModel.saveActivity(activity) }
            findNavController().popBackStack()
        } else {

            val activity = Activity(name = name)
            viewModel.addActivity(activity)
            findNavController().navigate(R.id.action_addEditFragment_to_activitiesFragment)
        }
    }

    private fun showDeleteDialog() {

        AlertDialog.Builder(context!!)
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

        return if (item.itemId == R.id.action_save) {

            viewModel.triggerSaveActivity()
            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }

    private fun setNameError(@StringRes resId: Int) = viewModel.setNameError(getString(resId))
}
