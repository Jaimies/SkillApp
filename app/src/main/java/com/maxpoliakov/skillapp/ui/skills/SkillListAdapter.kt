package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.adapter.ListAdapter
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupHeaderDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import com.maxpoliakov.skillapp.ui.skills.stopwatch.StopwatchDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.stopwatch.StopwatchUiModel
import javax.inject.Inject

const val ITEM_TYPE_SKILL = 0
const val ITEM_TYPE_SKILL_GROUP_HEADER = 1
const val ITEM_TYPE_STOPWATCH = 2

class SkillListAdapter(
    startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
    stopwatchDelegateAdapter: StopwatchDelegateAdapter,
    skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
    private val skillGroupHeaderDelegateAdapter: SkillGroupHeaderDelegateAdapter,
) : ListAdapter<Any, RecyclerView.ViewHolder>(SkillDiffCallback()) {

    private val skillDelegateAdapter = skillDelegateAdapterFactory.create(startDrag)

    val adapters = mapOf(
        ITEM_TYPE_SKILL to skillDelegateAdapter,
        ITEM_TYPE_SKILL_GROUP_HEADER to skillGroupHeaderDelegateAdapter,
        ITEM_TYPE_STOPWATCH to stopwatchDelegateAdapter,
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType]!!.onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        if (item is Skill)
            skillDelegateAdapter.onBindViewHolder(holder as SkillViewHolder, item)
        else if (item is SkillGroup)
            skillGroupHeaderDelegateAdapter.onBindViewHolder(holder as SkillGroupViewHolder, item)
    }

    override fun getItemViewType(position: Int): Int {
        if (position < 0 || position >= currentList.size) return -1
        if (getItem(position) is SkillGroup) return ITEM_TYPE_SKILL_GROUP_HEADER
        if (getItem(position) is Skill) return ITEM_TYPE_SKILL
        return ITEM_TYPE_STOPWATCH
    }

    @JvmName("submitSkillList")
    fun submitList(data: List<Any>) {
        if (stopwatchIsShown())
            super.submitList(listOf(StopwatchUiModel) + data)
        else
            super.submitList(data)
    }

    fun showStopwatch() {
        if (!stopwatchIsShown())
            super.submitList(listOf(StopwatchUiModel) + currentList)
    }

    fun hideStopwatch() {
        if (stopwatchIsShown())
            super.submitList(currentList.slice(1..currentList.lastIndex))
    }

    fun moveItem(from: Int, to: Int) {
        if (stopwatchIsShown() && (to == 0 || from == 0)) return
        val list = currentList.toMutableList()
        val item = list[from]
        list.removeAt(from)
        list.add(to, item)
        setListWithoutDiffing(list)
        notifyItemMoved(from, to)
    }

    private fun stopwatchIsShown(): Boolean {
        return itemCount != 0 && getItemViewType(0) == ITEM_TYPE_STOPWATCH
    }

    class Factory @Inject constructor(
        private val skillGroupHeaderDelegateAdapter: SkillGroupHeaderDelegateAdapter,
        private val skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
        private val stopwatchDelegateAdapter: StopwatchDelegateAdapter,
    ) {
        fun create(
            startDrag: (viewHolder: RecyclerView.ViewHolder) -> Unit,
        ) = SkillListAdapter(
            startDrag,
            stopwatchDelegateAdapter,
            skillDelegateAdapterFactory,
            skillGroupHeaderDelegateAdapter,
        )
    }
}
