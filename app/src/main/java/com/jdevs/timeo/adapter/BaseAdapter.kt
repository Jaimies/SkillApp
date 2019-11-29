package com.jdevs.timeo.adapter

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.adapter.delegates.LoadingDelegateAdapter
import com.jdevs.timeo.adapter.delegates.ViewType
import com.jdevs.timeo.adapter.delegates.ViewTypeDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants

open class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLastItemReached = false
    protected val items = ArrayList<ViewType>()
    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {

        override fun getViewType() = AdapterConstants.LOADING
    }

    init {

        delegateAdapters.put(AdapterConstants.LOADING, LoadingDelegateAdapter())

        items.add(loadingItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BaseViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.activities_item_loading,
                parent,
                false
            )
        )
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun getItemCount() = items.size

    fun addItem(item: ViewType) = items.apply {
        val initPosition = size - 1

        remove(loadingItem)
        notifyItemRemoved(initPosition)

        add(item)

        if (!isLastItemReached) {

            add(loadingItem)
        }

        notifyItemRangeInserted(initPosition, size - 1)
    }

    fun modifyItem(index: Int, item: ViewType) {

        items[index] = item
        notifyItemChanged(index)
    }

    fun removeItem(index: Int) {
        items.removeAt(index)
        notifyItemRemoved(index)
    }

    fun onLastItemReached() {

        isLastItemReached = true
        items.removeAt(items.size - 1)
    }

    fun removeAllItems() {

        items.clear()
        items.add(loadingItem)

        notifyDataSetChanged()
    }

    class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}