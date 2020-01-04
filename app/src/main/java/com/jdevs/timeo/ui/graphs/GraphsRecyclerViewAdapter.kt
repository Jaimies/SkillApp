package com.jdevs.timeo.ui.graphs

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.ListAdapter

class GraphsRecyclerViewAdapter : ListAdapter() {

    override val delegateAdapter = GraphDelegateAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapter.onCreateViewHolder(parent)
}
