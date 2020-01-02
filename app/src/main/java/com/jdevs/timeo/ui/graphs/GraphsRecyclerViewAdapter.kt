package com.jdevs.timeo.ui.graphs

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.ListAdapter

class GraphsRecyclerViewAdapter : ListAdapter() {

    override val delegateAdapter = GraphsDelegateAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapter.onCreateViewHolder(parent)
    }
}
