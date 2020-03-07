package com.jdevs.timeo.ui.addactivity

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.AddactivityFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.application
import com.jdevs.timeo.util.fragment.mainActivity
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hideKeyboard
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditActivityFragment : ActionBarFragment() {

    private val viewModel: AddEditActivityViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

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

        val binding = AddactivityFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        mainActivity.supportActionBar?.setTitle(if (isEdited) R.string.edit_activity else R.string.create_activity)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.showDeleteDialog) { showDeleteDialog() }
    }

    private fun saveActivity() {

        val name = viewModel.name.value.orEmpty()

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
                snackbar(R.string.activity_deleted)
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

    private fun setNameError(@StringRes resId: Int) = viewModel.setNameError(getString(resId))

    companion object {
        private const val NAME_MAX_LENGTH = 100
    }
}
