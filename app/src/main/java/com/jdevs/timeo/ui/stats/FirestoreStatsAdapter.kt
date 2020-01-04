package com.jdevs.timeo.ui.stats

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.FirestoreListAdapter

class FirestoreStatsAdapter : FirestoreListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapters.get(viewType).onCreateViewHolder(parent)
}
