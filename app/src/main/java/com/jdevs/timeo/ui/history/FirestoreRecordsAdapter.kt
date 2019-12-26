package com.jdevs.timeo.ui.history

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.FirestoreListAdapter

class FirestoreRecordsAdapter(
    private val showDeleteDialog: (Int) -> Unit = {}
) : FirestoreListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, showDeleteDialog = showDeleteDialog)
    }
}
