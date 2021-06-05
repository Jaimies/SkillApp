package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.common.BaseViewHolder

private const val ITEM_TYPE_SKILL = 0
private const val ITEM_TYPE_STOPWATCH = 1

class SkillListAdapter(
    private val skillDelegateAdapter: SkillDelegateAdapter,
    stopwatchDelegateAdapter: StopwatchDelegateAdapter,
) : ListAdapter<Any, BaseViewHolder>(SkillDiffCallback()) {

    val adapters = mapOf(
        ITEM_TYPE_SKILL to skillDelegateAdapter,
        ITEM_TYPE_STOPWATCH to stopwatchDelegateAdapter,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return adapters[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        val item = getItem(position)
        if (item !is Skill) return
        skillDelegateAdapter.onBindViewHolder(holder as SkillViewHolder, item)
    }

    fun getSkill(position: Int): Skill {
        val item = getItem(position)
        if (item !is Skill) return getItem(position + 1) as Skill
        return item
    }

    override fun getItemViewType(position: Int): Int {
        if (getItem(position) is Skill) return ITEM_TYPE_SKILL
        return ITEM_TYPE_STOPWATCH
    }

    @JvmName("submitSkillList")
    fun submitList(data: List<Skill>) {
        if (stopwatchIsShown())
            super.submitList(listOf(StopwatchUiModel) + data)
        else
            super.submitList(data)
    }

    fun showStopwatch() {
        if (!stopwatchIsShown())
            submitList(listOf(StopwatchUiModel) + currentList)
    }

    fun hideStopwatch() {
        if (stopwatchIsShown())
            submitList(currentList.slice(1..currentList.lastIndex))
    }

    private fun stopwatchIsShown(): Boolean {
        return itemCount != 0 && getItemViewType(0) == 1
    }
}
