package com.jdevs.timeo.ui.history

import com.jdevs.timeo.model.RecordItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.shared.util.isDateAfter
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter

class FirestoreHistoryAdapter(delegateAdapter: DelegateAdapter) :
    FirestoreListAdapter(delegateAdapter) {

    init {
        delegateAdapters.put(DATE_LABEL, DateLabelDelegateAdapter())
    }

    override fun addItem(item: ViewItem) {

        item as RecordItem
        val lastItem = items.lastOrNull { it is RecordItem } as RecordItem?

        if (dataItemCount == 0 || lastItem?.creationDate?.isDateAfter(item.creationDate) == true) {

            items += DateLabel(item.creationDate.toLocalDate())
            notifyItemInserted(items.lastIndex)
        }

        super.addItem(item)
    }

    override fun removeItem(item: ViewItem) {

        item as RecordItem

        val countOfItems = items.count {
            it is RecordItem && it.creationDate.toLocalDate() == item.creationDate.toLocalDate()
        }

        if (countOfItems == 1) {

            val labelIndex = items.indexOfFirst {
                it is DateLabel && it.date == item.creationDate.toLocalDate()
            }

            items.removeAt(labelIndex)
            notifyItemRemoved(labelIndex)
        }

        super.removeItem(item)
    }

    override fun getItemViewType(position: Int): Int {

        if (items[position] is DateLabel) return DATE_LABEL
        return super.getItemViewType(position)
    }

    companion object {
        private const val DATE_LABEL = 2
    }
}
