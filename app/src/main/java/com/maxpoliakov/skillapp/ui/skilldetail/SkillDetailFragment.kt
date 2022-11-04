package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
class SkillDetailFragment : DetailsFragment(R.menu.skilldetail_frag_menu) {
    override val content get() = binding.dataLayout
    override val input get() = binding.titleInput
    override val saveBtn get() = binding.saveFab
    override val goalInput get() = binding.goalPicker.root
    override val recyclerView get() = binding.history.recyclerView
    override val history get() = binding.history.root
    override val chart get() = binding.productivityChart.chart

    override val viewModel: SkillDetailViewModel by viewModels()

    @Inject
    lateinit var recordUtil: RecordUtil

    private lateinit var binding: SkilldetailFragBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SkilldetailFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.showRecordAdded) { record ->
            if (record != null) recordUtil.notifyRecordAdded(requireView(), record)
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
        viewModel.unit.value!!.showPicker(requireContext(), onTimeSet = viewModel::addRecord)
    }
}
