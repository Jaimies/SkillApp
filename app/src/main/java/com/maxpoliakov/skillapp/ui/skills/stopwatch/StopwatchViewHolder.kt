package com.maxpoliakov.skillapp.ui.skills.stopwatch

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import com.maxpoliakov.skillapp.util.ui.getBaseContext

class StopwatchViewHolder(
    private val binding: StopwatchBannerBinding,
    private val recordUtil: RecordUtil,
    viewModel: StopwatchViewModel,
) : BaseViewHolder(binding) {
    init {
        viewModel.navigateToSkill.observe(lifecycleOwner) { skill ->
            navigateToSkillDetail(binding.root, skill)
        }

        viewModel.showRecordAdded.observe(lifecycleOwner) { record ->
            record?.let(recordUtil::notifyRecordAdded)
        }
    }

    private fun navigateToSkillDetail(view: View, skill: Skill) {
        val directions = MainDirections.actionToSkillDetailFragment(skill.id)
        val transitionName = context.getString(R.string.skill_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        val activity = context.getBaseContext<AppCompatActivity>()
        val navController = activity.findNavController(R.id.nav_host_container)
        navController.navigate(directions, extras)
    }
}
