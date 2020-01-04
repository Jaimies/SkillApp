package com.jdevs.timeo.ui.stats

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.ListAdapter

class StatsRecyclerViewAdapter : ListAdapter() {

    override val delegateAdapter = StatisticDelegateAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapter.onCreateViewHolder(parent)
}
