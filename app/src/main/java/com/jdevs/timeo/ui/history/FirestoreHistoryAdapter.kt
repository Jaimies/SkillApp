package com.jdevs.timeo.ui.history

import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import org.threeten.bp.LocalDate

class FirestoreHistoryAdapter(delegateAdapter: DelegateAdapter) :
    FirestoreListAdapter(delegateAdapter) {

    init {
        delegateAdapters.put(DATE_LABEL, DateLabelDelegateAdapter())
    }

    override fun addItem(item: ViewItem, index: Int) {
        item as RecordItem
        val creationDate = item.creationDate.toLocalDate()

        addDateLabelIfNeeded(creationDate, index)

        val labelCount = items.count { it is DateLabel && it.date >= creationDate }

        super.addItem(item, labelCount + index)
    }

    override fun modifyItem(item: ViewItem, newIndex: Int) {
        item as RecordItem
        val count = items.count { it is DateLabel && it.date >= item.creationDate.toLocalDate() }
        super.modifyItem(item, newIndex + count)
    }

    override fun removeItem(item: ViewItem) {
        super.removeItem(item)
        item as RecordItem
        removeDateLabelIfNeeded(item.creationDate.toLocalDate())
    }

    private fun addDateLabelIfNeeded(date: LocalDate, index: Int) {
        val label = items.find { it is DateLabel && it.date == date }

        if (label == null) {
            val recordCount = items.count { it is DateLabel && it.date >= date }
            items.add(recordCount + index, DateLabel(date))
            notifyItemInserted(recordCount + index)
        }
    }

    private fun removeDateLabelIfNeeded(date: LocalDate) {
        val itemCount = items.count {
            it is RecordItem && it.creationDate.toLocalDate() == date
        }

        if (itemCount == 0) {
            val labelIndex = items.indexOfFirst { it is DateLabel && it.date == date }

            items.removeAt(labelIndex)
            notifyItemRemoved(labelIndex)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (items[position] is DateLabel) return DATE_LABEL
        return super.getItemViewType(position)
    }

    companion object {
        private const val DATE_LABEL = 2
    }
}
