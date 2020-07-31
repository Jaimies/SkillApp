package com.jdevs.timeo.ui.common.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

open class FirestoreListAdapter(delegateAdapter: DelegateAdapter) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(),
    BaseAdapter {

    val dataItemCount get() = items.filter { it != loadingItem }.size

    protected val delegateAdapters = SparseArray<DelegateAdapter>()
    protected val items = mutableListOf<ViewItem>()

    private val loadingItem = object : ViewItem {
        override val id = ""
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ITEM, delegateAdapter)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, items[position])
    }

    override fun getItemCount() = items.size
    override fun getItemViewType(position: Int) =
        if (items[position] == loadingItem) LOADING else ITEM

    override fun getItem(position: Int) = items[position]

    fun showLoader() {

        hideLoader()

        items.add(loadingItem)
        notifyItemInserted(items.lastIndex)
    }

    fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {

            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    @CallSuper
    open fun addItem(item: ViewItem, index: Int) {

        if (items.count { it.id == item.id } > 0) {
            modifyItem(item, index)
            return
        }

        items.add(index, item)
        notifyItemInserted(index)
    }

    @CallSuper
    open fun modifyItem(item: ViewItem, newIndex: Int) {

        val oldIndex = items.indexOfFirst { it.id == item.id }
        val oldItem = items[oldIndex]

        if (oldItem != item) {
            items[newIndex] = item
            notifyItemChanged(oldIndex)
        }

        if (newIndex != oldIndex) {
            items.removeAt(oldIndex)
            items.add(newIndex, item)
            notifyItemMoved(oldIndex, newIndex)
        }
    }

    @CallSuper
    open fun removeItem(item: ViewItem) {

        if (item !in items) {
            return
        }

        val index = items.indexOfFirst { it.id == item.id }

        items.removeAt(index)
        notifyItemRemoved(index)
    }

    companion object {

        private const val LOADING = 0
        private const val ITEM = 1
    }
}
