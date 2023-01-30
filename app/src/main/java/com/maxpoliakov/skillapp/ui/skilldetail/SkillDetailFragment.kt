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

    override val content get() = requireBinding().dataLayout
    override val input get() = requireBinding().titleInput
    override val saveBtn get() = requireBinding().saveFab
    override val goalInput get() = requireBinding().goalPicker.root
    override val recyclerView get() = requireBinding().history.recyclerView
    override val history get() = requireBinding().history.root
    override val SkilldetailFragBinding.chart get() = productivityChart.chart

    override val viewModel: SkillDetailViewModel by viewModels()

    @Inject
    lateinit var recordUtil: RecordUtil

    override fun onBindingCreated(binding: SkilldetailFragBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.showRecordAdded) { record ->
            record?.let(recordUtil::notifyRecordAdded)
        }
    }

    override fun onStartEditing() = logEvent("edit_skill")

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
        viewModel.unit.value!!.showPicker(childFragmentManager, onTimeSet = viewModel::addRecord)
    }
}
