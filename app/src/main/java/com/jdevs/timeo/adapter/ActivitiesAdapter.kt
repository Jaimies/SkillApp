package com.jdevs.timeo.adapter

import android.util.SparseArray
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapter.delegate.ActivityDelegateAdapter
import com.jdevs.timeo.adapter.delegate.AdapterConstants.ACTIVITY
import com.jdevs.timeo.adapter.delegate.AdapterConstants.LOADING
import com.jdevs.timeo.adapter.delegate.LoadingDelegateAdapter
import com.jdevs.timeo.adapter.delegate.ViewType
import com.jdevs.timeo.adapter.delegate.ViewTypeDelegateAdapter
import com.jdevs.timeo.data.ActivityHolder
import com.jdevs.timeo.data.TimeoActivity

class ActivitiesAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val activityList = ArrayList<ActivityHolder>()

    private val delegateAdapters = SparseArray<ViewTypeDelegateAdapter>()

    private val loadingItem = ActivityHolder(null, LOADING)

    init {
        delegateAdapters.put(LOADING, LoadingDelegateAdapter())
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())

        activityList.add(loadingItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType).onCreateViewHolder(parent)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position)).onBindViewHolder(holder, object : ViewType {
            override fun getViewType() = ACTIVITY
        })
    }

    override fun getItemViewType(position: Int) = activityList[position].type

    override fun getItemCount() = activityList.size

    fun addItem(item: TimeoActivity) = activityList.apply {
        removeAt(size - 1)
        add(ActivityHolder(item, ACTIVITY))
        add(loadingItem)
    }
}