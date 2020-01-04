package com.jdevs.timeo.ui.history

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.ListAdapter

class RecordsAdapter(
    private val showDeleteDialog: (Int) -> Unit = {}
) : ListAdapter() {

    override val delegateAdapter = RecordDelegateAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapter.onCreateViewHolder(parent, showDeleteDialog = showDeleteDialog)
}
