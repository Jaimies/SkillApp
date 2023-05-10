package com.maxpoliakov.skillapp.ui.skills.recyclerview.skill

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.OnRebindCallback
import androidx.lifecycle.asLiveData
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.shared.recyclerview.BaseViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListViewHolder
import com.maxpoliakov.skillapp.shared.tracking.RecordUtil
import kotlinx.coroutines.flow.drop

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    private val recordUtil: RecordUtil,
    callback: SkillsFragmentCallback,
) : BaseViewHolder(binding), SkillListViewHolder {
    private val viewModel = binding.viewModel!!

    private var shouldAnimateLayoutChanges = false

    private val onRebindCallback = object : OnRebindCallback<SkillsItemBinding>() {
        override fun onPreBind(binding: SkillsItemBinding): Boolean {
            if (shouldAnimateLayoutChanges) {
                beginDelayedTransition(binding)
                shouldAnimateLayoutChanges = false
            }

            return super.onPreBind(binding)
        }

        private fun beginDelayedTransition(binding: SkillsItemBinding) {
            TransitionManager.beginDelayedTransition(
                binding.root as ViewGroup,
                AutoTransition().apply { duration = 150 },
            )
        }
    }

    init {
        binding.dragHandleWrapper.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                return@setOnTouchListener viewModel.startDrag()
            }

            false
        }

        binding.addOnRebindCallback(onRebindCallback)

        viewModel.dragHandleShown.drop(1).asLiveData().observe(lifecycleOwner) {
            shouldAnimateLayoutChanges = true
        }

        viewModel.startDrag.observe(lifecycleOwner) {
            callback.startDrag(this)
        }

        viewModel.navigateToDetails.observe(lifecycleOwner) { skill ->
            callback.navigateToSkillDetail(binding.card, skill)
        }

        viewModel.showRecordsAdded.observe(lifecycleOwner, recordUtil::notifyRecordsAdded)
    }

    fun setItem(item: Skill) = viewModel.setSkill(item)

    var isSmall: Boolean
        get() = viewModel.isSmall.value!!
        set(value) = viewModel.setIsSmall(value)

    var isHighlighted: Boolean
        get() = viewModel.isHighlighted.value!!
        set(value) = viewModel.setIsHighlighted(value)

    override val groupId get() = viewModel.groupId
    override val unit get() = viewModel.skill.value!!.unit
    val isInAGroup get() = groupId != -1
}
