package com.jdevs.timeo.common.adapter

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.util.AdapterConstants.LOADING

@Suppress("TooManyFunctions")
abstract class ItemListAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.getViewType() != LOADING }.size

    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    protected val items = mutableListOf<ViewType>()
    private val ids = mutableListOf<String>()
    private var isLastItemReached = false

    private val loadingItem = object : ViewType {

        override fun getViewType() = LOADING
    }

    init {

        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        items.add(loadingItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()
    override fun getItemCount() = items.size

    fun getItem(index: Int) = items[index]
    fun getId(index: Int): String = ids[index]

    fun addItem(item: ViewType, id: String) = items.apply {

        if (ids.contains(id)) {

            modifyItem(item, id)
            return@apply
        }

        hideLoader()
        add(item)
        ids.add(id)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: ViewType, id: String) {

        val index = ids.indexOf(id)

        if (items[index] != item) {

            items[index] = item
            notifyItemChanged(index)
        }
    }

    fun removeItem(id: String) {

        val index = ids.indexOf(id)

        items.removeAt(index)
        ids.removeAt(index)

        notifyItemRemoved(index)
    }

    fun onLastItemReached() {

        isLastItemReached = true
        hideLoader()
    }

    fun showLoader() {
        if (!isLastItemReached && !items.contains(loadingItem)) {

            items.add(loadingItem)
            notifyItemInserted(items.lastIndex)
        }
    }

    private fun hideLoader() {

        val index = items.indexOf(loadingItem)

        if (index != -1) {
            items.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
