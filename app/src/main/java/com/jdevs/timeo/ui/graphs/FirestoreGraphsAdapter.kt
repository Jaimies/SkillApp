package com.jdevs.timeo.ui.graphs

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.FirestoreListAdapter

class FirestoreGraphsAdapter : FirestoreListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapters.get(viewType).onCreateViewHolder(parent)
}
