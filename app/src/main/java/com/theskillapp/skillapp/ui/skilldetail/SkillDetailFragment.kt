package com.theskillapp.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.launch
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.SkilldetailFragBinding
import com.theskillapp.skillapp.shared.DetailsFragment
import com.theskillapp.skillapp.shared.dialog.showDialog
import com.theskillapp.skillapp.shared.fragment.observe
import com.theskillapp.skillapp.shared.permissions.PermissionRequester
import com.theskillapp.skillapp.shared.tracking.RecordUtil
import com.theskillapp.skillapp.shared.fragment.addKeepScreenOnFlag
import com.theskillapp.skillapp.shared.fragment.removeKeepScreenOnFlag
import com.theskillapp.skillapp.domain.repository.UserPreferenceRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillDetailFragment : DetailsFragment<SkilldetailFragBinding>(R.menu.skilldetail_frag_menu) {
    override val layoutId get() = R.layout.skilldetail_frag

    override val viewModel: SkillDetailViewModel by viewModels()

    override val SkilldetailFragBinding.content get() = dataLayout
    override val SkilldetailFragBinding.input get() = titleInput
    override val SkilldetailFragBinding.saveBtn get() = saveFab
    override val SkilldetailFragBinding.goalInput get() = goalPicker.root
    override val SkilldetailFragBinding.history get() = history.root
    override val SkilldetailFragBinding.recyclerView get() = history.recyclerView

    @Inject
    lateinit var recordUtil: RecordUtil

    @Inject
    lateinit var permissionRequester: PermissionRequester

    @Inject
    lateinit var userPreferenceRepository: UserPreferenceRepository

    override fun onBindingCreated(binding: SkilldetailFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.showRecordAdded, recordUtil::notifyRecordsAdded)

        observe(viewModel.showRecordDialog) {
            showRecordDialog()
        }

        observe(viewModel.stopwatchStarted) {
            permissionRequester.requestNotificationPermissionIfNotGranted()
        }

        if (userPreferenceRepository.keepScreenOn) {
            keepScreenOnWhenNeeded()
        }
    }

    private fun keepScreenOnWhenNeeded() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.timer
                .flowWithLifecycle(viewLifecycleOwner.lifecycle, Lifecycle.State.STARTED)
                .collect { timer ->
                    if (timer != null) addKeepScreenOnFlag()
                    else removeKeepScreenOnFlag()
                }
        }
    }

    override fun onPause() {
        super.onPause()
        removeKeepScreenOnFlag()
    }

    override fun onDeleteSelected() {
        requireContext().showDialog(R.string.delete_skill_title, R.string.delete_skill_message, R.string.delete) {
            deleteSkill()
        }
    }

    private fun deleteSkill() {
        viewModel.deleteSkill()
        findNavController().popBackStack()
    }

    private fun showRecordDialog() {
        viewModel.unit.value!!.showPicker(
            childFragmentManager,
            initialCount = viewModel.latestRecord.value?.count ?: 0,
            onTimeSet = viewModel::addRecord,
        )
    }
}
