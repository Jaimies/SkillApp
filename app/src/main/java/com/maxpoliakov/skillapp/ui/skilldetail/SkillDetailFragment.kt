package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkilldetailFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
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

    override fun onBindingCreated(binding: SkilldetailFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.showRecordAdded) { record ->
            record?.let(recordUtil::notifyRecordAdded)
        }
    }

    override fun onSwitchToEditMode() = logEvent("edit_skill")

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
