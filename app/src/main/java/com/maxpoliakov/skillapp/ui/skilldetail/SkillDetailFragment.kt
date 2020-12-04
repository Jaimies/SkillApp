package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkilldetailFragBinding
import com.maxpoliakov.skillapp.domain.model.Record
import com.maxpoliakov.skillapp.ui.common.ActionBarFragment
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.setTitle
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.fragment.snackbar
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import com.maxpoliakov.skillapp.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.chart.chart
import javax.inject.Inject

@AndroidEntryPoint
class SkillDetailFragment : ActionBarFragment(R.menu.skilldetail_frag_menu) {
    private val viewModel by viewModels { viewModelFactory.create(args.skillId) }

    @Inject
    lateinit var viewModelFactory: SkillDetailViewModel.Factory

    private val args: SkillDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SkilldetailFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        chart.setup()
        observe(viewModel.stats, chart::setState)
        observe(viewModel.showRecordDialog) { showRecordDialog() }
        observe(viewModel.skill) { skill -> setTitle(skill.name) }
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
            .actionToEditSkillFragment(viewModel.skill.value!!)

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
        snackbar(R.string.skill_deleted_message)
        findNavController().popBackStack()
    }

    private fun showRecordDialog() {
        showTimePicker { duration ->
            val skill = viewModel.skill.value!!
            viewModel.addRecord(Record(skill.name, skill.id, duration))
        }
    }
}
