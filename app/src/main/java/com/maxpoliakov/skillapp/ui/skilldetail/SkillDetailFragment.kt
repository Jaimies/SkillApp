package com.maxpoliakov.skillapp.ui.skilldetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkilldetailFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.dialog.showDialog
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.fragment.showTimePicker
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.hardware.showKeyboard
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SkillDetailFragment : DetailsFragment(R.menu.skilldetail_frag_menu) {
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
        observe(viewModel.onSave) { stopEditing() }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = !onBackPressed()
        }
    }

    private fun onBackPressed(): Boolean {
        if (viewModel.isEditing.value!!) {
            stopEditing()
            return false
        } else {
            findNavController().navigateUp()
            return true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        when (item.itemId) {
            R.id.edit -> {
                if (viewModel.isEditing.value!!)
                    stopEditing()
                else
                    startEditing()
            }
            R.id.delete -> showDeleteDialog()
            else -> return false
        }

        return true
    }

    private fun startEditing() = binding.titleInput.run {
        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setSelection(text.length)
        showKeyboard()

        viewModel.enterEditingMode()

        menu.getItem(0).setTitle(R.string.save)

        val duration = resources.getInteger(R.integer.animation_duration).toLong()
        binding.dataLayout.animate()
            .alpha(0f)
            .translationYBy(30.dp.toPx(requireContext()).toFloat())
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            binding.dataLayout.isGone = true
        }

        binding.saveFab.isGone = false
        binding.saveFab.alpha = 0f

        binding.saveFab.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }

    private fun stopEditing() = binding.titleInput.run {
        isFocusable = false
        isFocusableInTouchMode = false
        clearFocus()
        hideKeyboard()

        viewModel.exitEditingMode()
        menu.getItem(0).setTitle(R.string.edit)
        val duration = resources.getInteger(R.integer.animation_duration).toLong()

        binding.dataLayout.isGone = false

        binding.dataLayout.animate()
            .alpha(1f)
            .translationYBy(-30.dp.toPx(context).toFloat())
            .setDuration(duration)
            .start()

        binding.saveFab.animate()
            .alpha(0f)
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            binding.saveFab.isGone = true
        }
    }

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
