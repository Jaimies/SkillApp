package com.maxpoliakov.skillapp.ui.skills.recyclerview

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.ItemChangeNotificationStrategy
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.ListAdapter
import com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview.FakeCardViewDecoration
import com.maxpoliakov.skillapp.ui.skills.SkillsFragmentCallback
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooterDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.header.SkillGroupHeaderDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.skill.SkillDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch.StopwatchDelegateAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.stopwatch.StopwatchUiModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Provider

class SkillListAdapter @AssistedInject constructor(
    @Assisted
    callback: SkillsFragmentCallback,
    private val lifecycleOwnerProvider: Provider<LifecycleOwner>,
    stopwatchDelegateAdapterFactory: StopwatchDelegateAdapter.Factory,
    skillDelegateAdapterFactory: SkillDelegateAdapter.Factory,
) : ListAdapter<Any, RecyclerView.ViewHolder>(SkillDiffCallback()) {

    val adapters = mapOf(
        ItemType.Skill to skillDelegateAdapterFactory.create(callback),
        ItemType.SkillGroupHeader to SkillGroupHeaderDelegateAdapter(callback),
        ItemType.SkillGroupFooter to SkillGroupFooterDelegateAdapter(),
        ItemType.Stopwatch to stopwatchDelegateAdapterFactory.create(callback),
    ) as Map<Int, DelegateAdapter<Any, RecyclerView.ViewHolder>>

    init {
        setHasStableIds(true)
    }

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

    override fun getItemId(position: Int): Long {
        // will be simplified when a separate model for skill list items is introduced
        return when (val item = getItem(position)) {
            is Skill -> getSkillItemId(item.id)
            is SkillGroup -> getGroupItemId(item.id)
            is SkillGroupFooter -> getGroupFooterItemId(item.group.id)
            is StopwatchUiModel -> getStopwatchItemId()
            else -> RecyclerView.NO_ID
        }
    }

    private fun isOutOfBounds(position: Int): Boolean {
        return position < 0 || position >= currentList.size
    }

    fun addItem(position: Int, item: Any) {
        if (isOutOfBounds(position)) return

        val list = currentList.toMutableList().apply {
            add(position, item)
        }

        setListWithoutDiffing(list)
        notifyItemInserted(position)
    }

    fun moveItem(from: Int, to: Int) {
        if (isOutOfBounds(from) || isOutOfBounds(to)) {
            return
        }

        val list = currentList.toMutableList()
        val item = list[from]
        list.removeAt(from)
        list.add(to, item)
        setListWithoutDiffing(list)
        notifyItemMoved(from, to)
    }

    inline fun <reified T : Any> bindItemWithItemIdToViewHolder(itemId: Long, recyclerView: RecyclerView?, transform: (T) -> T) {
        updateItemWithItemId(itemId, ItemChangeNotificationStrategy.OnBindViewHolder(recyclerView), transform)
    }

    inline fun <reified T : Any> updateItemWithItemId(
        itemId: Long,
        notificationStrategy: ItemChangeNotificationStrategy = ItemChangeNotificationStrategy.NotifyItemChanged,
        transform: (T) -> T,
    ) {
        val position = findItemPositionByItemId(itemId)
        if (position == -1) return

        val newList = currentList.toMutableList()
        val item = newList[position] as? T ?: return
        newList[position] = transform(item)
        setListWithoutDiffing(newList)
        notificationStrategy.notifyOfItemChange(this, position)
    }

    object ItemType {
        const val Skill = 0
        const val Stopwatch = 3

        const val SkillGroupHeader = FakeCardViewDecoration.HEADER_VIEW_TYPE
        const val SkillGroupFooter = FakeCardViewDecoration.FOOTER_VIEW_TYPE
    }

    companion object {
        fun getSkillItemId(skillId: Int) = skillId.toLong()
        fun getGroupItemId(groupId: Int) = 1_000_000L + groupId.toLong()
        fun getGroupFooterItemId(groupId: Int) = -getGroupItemId(groupId)
        fun getStopwatchItemId() = 0L
    }

    @AssistedFactory
    interface Factory {
        fun create(callback: SkillsFragmentCallback): SkillListAdapter
    }
}
