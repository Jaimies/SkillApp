package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkilldetailFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.ui.GoalPicker
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillDetailFragment : DetailsFragment(R.menu.skilldetail_frag_menu) {
    override val content get() = binding.dataLayout
    override val input get() = binding.titleInput
    override val saveBtn get() = binding.saveFab

    override val viewModel by viewModels { viewModelFactory.create(args.skillId) }

    override val chart get() = binding.productivityChart.chart

    @Inject
    lateinit var viewModelFactory: SkillDetailViewModel.Factory

    @Inject
    lateinit var recordUtil: RecordUtil

    private lateinit var binding: SkilldetailFragBinding

    private val args: SkillDetailFragmentArgs by navArgs()

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

        observe(viewModel.chartData.statisticType) { type ->
            binding.productivityChart.chart.update(type, viewModel.chartData, viewLifecycleOwner)
        }
        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.showRecordAdded) { record ->
            if (record != null) recordUtil.notifyRecordAdded(requireView(), record)
        }

        observe(viewModel.chooseGoal) {
            val picker = GoalPicker.Builder()
                .setGoal(viewModel.goal.value)
                .build()

            picker.show(requireContext().getFragmentManager(), null)
            picker.addOnPositiveButtonClickListener(View.OnClickListener {
                viewModel.setGoal(picker.goal)
            })
        }
    }

    override fun startEditing() {
        super.startEditing()
        binding.goalTextInput.isVisible = true
        binding.goalTextInput.alpha = 0f
        binding.goalTextInput.animate()
            .setDuration(getTransitionDuration())
            .translationYBy(-30.dp.toPx(requireContext()).toFloat())
            .alpha(1f)
            .start()
    }

    override fun stopEditing() {
        super.stopEditing()
        binding.goalTextInput.isVisible = false
        binding.goalTextInput.translationY = 0f
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete) {
            showDeleteDialog()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStartEditing() = logEvent("edit_skill")

    private fun showDeleteDialog() {
        requireContext().showDialog(R.string.delete_skill_title, R.string.delete_skill_message, R.string.delete) {
            deleteSkill()
        }
    }

    private fun deleteSkill() {
        viewModel.deleteSkill()
        findNavController().popBackStack()
    }

    private fun showRecordDialog() {
        showTimePicker(viewModel::addRecord)
    }
}
