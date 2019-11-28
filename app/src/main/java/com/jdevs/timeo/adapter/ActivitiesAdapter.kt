package com.jdevs.timeo.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapter.delegates.ActivityDelegateAdapter
import com.jdevs.timeo.adapter.delegates.AdapterConstants.ACTIVITY
import com.jdevs.timeo.adapter.delegates.AdapterConstants.LOADING
import com.jdevs.timeo.adapter.delegates.LoadingDelegateAdapter
import com.jdevs.timeo.adapter.delegates.ViewType
import com.jdevs.timeo.adapter.delegates.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.TimeoActivity

class ActivitiesAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var isLastItemReached = false
    private val activityList = ArrayList<ViewType>()
    private val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    private val loadingItem = object : ViewType {

        override fun getViewType() = LOADING
    }

    init {
        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())

        activityList.add(loadingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, createRecord, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, activityList[position])
    }

    override fun getItemViewType(position: Int) = activityList[position].getViewType()

    override fun getItemCount() = activityList.size

    fun addItem(item: TimeoActivity) = activityList.apply {
        val initPosition = size - 1

        removeAt(initPosition)
        notifyItemRemoved(initPosition)

        add(item)

        if (!isLastItemReached) {

            add(loadingItem)
        }

        notifyItemRangeInserted(initPosition, size - 1)
    }

    fun modifyItem(index: Int, item: TimeoActivity) {

        activityList[index] = item
        notifyItemChanged(index)
    }

    fun removeItem(index: Int) {
        activityList.removeAt(index)
        notifyItemRemoved(index)
    }

    fun onLastItemReached() {

        isLastItemReached = true
        activityList.removeAt(activityList.size - 1)
    }
}