package com.jdevs.timeo.common.adapter

import android.util.SparseArray
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.util.AdapterConstants.LOADING

abstract class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val dataItemCount get() = items.filter { it.getViewType() != LOADING }.size

    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    protected val items = mutableListOf<ViewType>()
    private val idList = mutableListOf<String>()
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

    fun addItem(item: ViewType, id: String) = items.apply {

        if (idList.contains(id)) {

            modifyItem(item, id)
            return@apply
        }

        hideLoader()
        add(item)
        idList.add(id)

        notifyItemInserted(lastIndex)
    }

    fun modifyItem(item: ViewType, id: String) {

        val index = idList.indexOf(id)

        items[index] = item
        notifyItemChanged(index)
    }

    fun removeItem(id: String) {

        val index = idList.indexOf(id)

        items.removeAt(index)
        idList.removeAt(index)

        notifyItemRemoved(index)
    }

    fun getItem(index: Int) = items[index]
    fun getId(index: Int): String = idList[index]

    fun onLastItemReached() {

        isLastItemReached = true
        hideLoader()
    }

    fun showLoader() {
        if (!isLastItemReached) {

            items.add(loadingItem)
            notifyItemInserted(items.lastIndex)
        }
    }

    private fun hideLoader() {
        if (items.contains(loadingItem)) {
            val lastPosition = items.lastIndex

            items.remove(loadingItem)
            notifyItemRemoved(lastPosition)
        }
    }
}
