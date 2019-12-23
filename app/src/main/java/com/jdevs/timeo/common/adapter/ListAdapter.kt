package com.jdevs.timeo.common.adapter

import android.content.Context
import android.util.SparseArray
import android.view.View
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.ui.activities.adapter.ActivityDelegateAdapter
import com.jdevs.timeo.ui.activities.adapter.RecordDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.AdapterConstants.LOADING
import com.jdevs.timeo.util.AdapterConstants.RECORD

@Suppress("TooManyFunctions")
abstract class ListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.getViewType() != LOADING }.size

    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    private val items = mutableListOf<ViewType>()

    private val loadingItem = object : ViewType {

        override var id = -1
        override var documentId = ""
        override fun getViewType() = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())
        delegateAdapters.put(RECORD, RecordDelegateAdapter())
        items.add(loadingItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()
    override fun getItemCount() = items.size
    fun getItem(index: Int) = items[index]

    fun showLoader() {

        if (!items.contains(loadingItem)) {

            items.add(loadingItem)
            notifyItemInserted(items.lastIndex)
        }
    }

    fun addItem(item: ViewType) = items.apply {

        if (items.count { it.documentId == item.documentId } > 0) {

            modifyItem(item)
            return@apply
        }

        hideLoader()
        add(item)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: ViewType) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(item: ViewType) {

        val index = items.indexOfFirst { it.documentId == item.documentId }

        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun setItems(newItems: List<ViewType>) {

        val diffResult = DiffUtil.calculateDiff(DiffCallback(newItems, items))

        items.clear()
        items.addAll(newItems)

        diffResult.dispatchUpdatesTo(this)
    }

    fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    class DiffCallback(
        private var newItems: List<ViewType>,
        private var oldItems: List<ViewType>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldItems.size

        override fun getNewListSize() = newItems.size

        override fun areItemsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {

            return oldItems[oldItemPosition].id == newItems[newItemPosition].id
        }

        override fun areContentsTheSame(
            oldItemPosition: Int,
            newItemPosition: Int
        ): Boolean {

            return oldItems[oldItemPosition] == newItems[newItemPosition]
        }
    }

    abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        protected val context: Context = view.context
        protected val lifecycleOwner = context as LifecycleOwner
    }
}
