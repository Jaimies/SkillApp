package com.jdevs.timeo.ui.activities

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.ListAdapter

class ActivitiesAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : ListAdapter() {

    override val delegateAdapter = ActivityDelegateAdapter()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return delegateAdapter.onCreateViewHolder(parent, createRecord, navigateToDetails)
    }
}
