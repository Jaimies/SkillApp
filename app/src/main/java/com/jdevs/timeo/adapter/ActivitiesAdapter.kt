package com.jdevs.timeo.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapter.delegates.ActivityDelegateAdapter
import com.jdevs.timeo.adapter.delegates.LoadingDelegateAdapter
import com.jdevs.timeo.util.AdapterConstants.ACTIVITY
import com.jdevs.timeo.util.AdapterConstants.LOADING

class ActivitiesAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : BaseAdapter() {

    init {
        delegateAdapters.put(ACTIVITY, ActivityDelegateAdapter())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, createRecord, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        delegateAdapters.get(getItemViewType(position))
            .onBindViewHolder(holder, items[position])
    }

    override fun getItemViewType(position: Int) = items[position].getViewType()

    override fun getItemCount() = items.size
}