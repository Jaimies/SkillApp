package com.jdevs.timeo.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.adapter.delegates.LoadingDelegateAdapter
import com.jdevs.timeo.adapter.delegates.ViewType
import com.jdevs.timeo.adapter.delegates.ViewTypeDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants
import com.jdevs.timeo.util.inflate

open class BaseAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items = mutableListOf<ViewType>()
    private val idList = mutableListOf<String>()

    protected val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()
    private var isLastItemReached = false

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

        return BaseViewHolder(parent.inflate(R.layout.activities_item_loading))
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()
    override fun getItemCount() = items.size

    fun addItem(item: ViewType, id: String) = items.apply {

        remove(loadingItem)

        add(item)
        idList.add(id)

        if (!isLastItemReached) {

            add(loadingItem)
        }

        notifyDataSetChanged()
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

    fun <T : ViewType> getItem(index: Int): T = items[index] as T

    fun getId(index: Int): String = idList[index]

    fun removeAllItems() {

        items.clear()
        idList.clear()
        items.add(loadingItem)

        notifyDataSetChanged()
    }

    fun onLastItemReached() {

        val lastPosition = items.lastIndex
        isLastItemReached = true

        items.removeAt(lastPosition)
        notifyItemRemoved(lastPosition)
    }

    class BaseViewHolder(view: View) : RecyclerView.ViewHolder(view)
}