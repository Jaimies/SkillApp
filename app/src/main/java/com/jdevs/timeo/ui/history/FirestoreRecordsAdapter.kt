package com.jdevs.timeo.ui.history

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.FirestoreListAdapter

class FirestoreRecordsAdapter(
    private val showDeleteDialog: (Int) -> Unit = {}
) : FirestoreListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, showDeleteDialog = showDeleteDialog)
}
