package com.maxpoliakov.skillapp.ui.skills

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.common.LifecycleOwnerProvider
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.common.adapter.ListAdapter
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupHeaderDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.stopwatch.StopwatchDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.stopwatch.StopwatchUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class SkillListAdapter @AssistedInject constructor(
    @Assisted
    callback: SkillsFragmentCallback,
    @Assisted
    private val lifecycleOwnerProvider: LifecycleOwnerProvider,
    stopwatchDelegateAdapter: StopwatchDelegateAdapter,
    skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
) : ListAdapter<Any, RecyclerView.ViewHolder>(SkillDiffCallback()) {

    val adapters = mapOf(
        ItemType.Skill to skillDelegateAdapterFactory.create(callback),
        ItemType.SkillGroupHeader to SkillGroupHeaderDelegateAdapter(callback),
        ItemType.SkillGroupFooter to SkillGroupFooterDelegateAdapter(),
        ItemType.Stopwatch to stopwatchDelegateAdapter,
    ) as Map<Int, DelegateAdapter<Any, RecyclerView.ViewHolder>>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return adapters[viewType]!!
            .onCreateViewHolder(parent, lifecycleOwnerProvider.get())
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapters[getItemViewType(position)]!!
            .onBindViewHolder(holder, getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        if (isOutOfBounds(position)) return -1

        val item = getItem(position)

        return when (item) {
            is Skill -> ItemType.Skill
            is SkillGroup -> ItemType.SkillGroupHeader
            is SkillGroupFooter -> ItemType.SkillGroupFooter
            is StopwatchUiModel -> ItemType.Stopwatch
            else -> throw IllegalStateException("Item of unsupported type ${item::class.simpleName}")
        }
    }

    private fun isOutOfBounds(position: Int): Boolean {
        return position < 0 || position >= currentList.size
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

    fun addItem(position: Int, item: Any) {
        val list = currentList.toMutableList().apply {
            add(position, item)
        }

        setListWithoutDiffing(list)
        notifyItemInserted(position)
    }

    fun moveItem(from: Int, to: Int) {
        if (from < 0 || from > currentList.lastIndex ||
            stopwatchIsShown() && (to == 0 || from == 0)
        ) return

        val list = currentList.toMutableList()
        val item = list[from]
        list.removeAt(from)
        list.add(to, item)
        setListWithoutDiffing(list)
        notifyItemMoved(from, to)
    }

    fun updateSilently(position: Int, item: Any) {
        val newList = currentList.toMutableList().apply {
            this[position] = item
        }

        setListWithoutDiffing(newList)
    }

    private fun stopwatchIsShown(): Boolean {
        return itemCount != 0 && getItemViewType(0) == ItemType.Stopwatch
    }

    object ItemType {
        const val Skill = 0
        const val SkillGroupHeader = 1
        const val SkillGroupFooter = 2
        const val Stopwatch = 3
    }

    @AssistedFactory
    interface Factory {
        fun create(callback: SkillsFragmentCallback, lifecycleOwnerProvider: LifecycleOwnerProvider): SkillListAdapter
    }
}
