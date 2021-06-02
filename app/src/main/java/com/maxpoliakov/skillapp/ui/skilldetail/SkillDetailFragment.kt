package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkilldetailFragBinding
import com.maxpoliakov.skillapp.model.mapToPresentation
import com.maxpoliakov.skillapp.ui.MainActivity
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setMarginTop
import com.maxpoliakov.skillapp.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SkillDetailFragment : ActionBarFragment(R.menu.skilldetail_frag_menu) {
    private val viewModel by viewModels { viewModelFactory.create(args.skillId) }

    @Inject
    lateinit var viewModelFactory: SkillDetailViewModel.Factory

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
        binding.productivityChart.chart.setup()
        observe(viewModel.statsChartData, binding.productivityChart.chart::setState)
        observe(viewModel.showRecordDialog) { showRecordDialog() }
    }

    override fun onStart() = binding.collapsingToolbarLayout.run {
        super.onStart()
        appBar.post {
            appBar.setMarginTop(-collapsingToolbar.height)
        }

        viewModel.skillLiveData.observe(viewLifecycleOwner) { skill ->
            toolbarLayout.title = skill.name
        }
        lifecycleScope.launch {
            awaitUntilAnimationFinished()
            (requireActivity() as MainActivity).setToolbar(collapsingToolbar)
            appBar.setMarginTop(0)
        }

        Unit
    }

    private suspend fun awaitUntilAnimationFinished() {
        val animationDuration = requireContext().resources.getInteger(R.integer.animation_duration)
        delay(animationDuration.toLong())
    }

    override fun onStop() = binding.collapsingToolbarLayout.run {
        super.onStop()
        (requireActivity() as MainActivity).resetToolbar()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.edit -> navigateToEditSkillFragment()
            R.id.delete -> showDeleteDialog()
            else -> return false
        }

        return true
    }

    private fun navigateToEditSkillFragment() {
        val directions = MainDirections
            .actionToEditSkillFragment(viewModel.skill.replayCache.last().mapToPresentation())

        findNavController().navigateAnimated(directions)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.delete_skill_title)
            .setMessage(R.string.delete_skill_message)
            .setPositiveButton(R.string.delete) { _, _ -> deleteSkill() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun deleteSkill() {
        viewModel.deleteSkill()
        findNavController().popBackStack()
    }

    private fun showRecordDialog() {
        showTimePicker(viewModel::addRecord)
    }
}
