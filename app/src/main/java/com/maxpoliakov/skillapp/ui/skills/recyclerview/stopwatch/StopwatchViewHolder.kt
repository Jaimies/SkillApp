package com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.StopwatchBannerBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class StopwatchViewHolder @AssistedInject constructor(
    @Assisted
    private val binding: StopwatchBannerBinding,
    viewModel: StopwatchViewModel,
    private val recordUtil: RecordUtil,
    private val navController: NavController,
) : BaseViewHolder(binding) {
    init {
        binding.viewModel = viewModel

        viewModel.navigateToSkill.observe(lifecycleOwner) { skill ->
            navigateToSkillDetail(binding.root, skill)
        }

        viewModel.showRecordsAdded.observe(lifecycleOwner, recordUtil::notifyRecordsAdded)
    }

    private fun navigateToSkillDetail(view: View, skill: Skill) {
        val directions = MainDirections.actionToSkillDetailFragment(skill.id)
        val transitionName = context.getString(R.string.skill_transition_name)
        val extras = FragmentNavigatorExtras(view to transitionName)
        navController.navigate(directions, extras)
    }

    @AssistedFactory
    interface Factory {
        fun create(binding: StopwatchBannerBinding): StopwatchViewHolder
    }
}
