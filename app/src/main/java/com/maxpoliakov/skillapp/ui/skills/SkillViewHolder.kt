package com.maxpoliakov.skillapp.ui.skills

import android.view.MotionEvent
import android.view.ViewGroup
import androidx.databinding.OnRebindCallback
import androidx.lifecycle.asLiveData
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.maxpoliakov.skillapp.databinding.SkillsItemBinding
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.util.tracking.RecordUtil
import kotlinx.coroutines.flow.drop

class SkillViewHolder(
    private val binding: SkillsItemBinding,
    private val recordUtil: RecordUtil,
    callback: SkillsFragmentCallback,
) : SkillListViewHolder(binding.root) {
    val viewModel = binding.viewModel!!

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
        binding.dragHandle.setOnTouchListener { view, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                view.performClick()
                callback.startDrag(this)
            }

            false
        }

        binding.addOnRebindCallback(onRebindCallback)

        viewModel.dragHandleShown.drop(1).asLiveData().observe {
            shouldAnimateLayoutChanges = true
        }

        viewModel.startDrag.observe {
            callback.startDrag(this)
        }

        viewModel.navigateToDetails.observe { skill ->
            callback.navigateToSkillDetail(binding.card, skill)
        }

        viewModel.notifyRecordAdded.observe { record ->
            recordUtil.notifyRecordAdded(binding.root, record)
        }
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
