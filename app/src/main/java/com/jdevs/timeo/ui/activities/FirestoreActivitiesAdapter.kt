package com.jdevs.timeo.ui.activities

import android.view.ViewGroup
import com.jdevs.timeo.common.adapter.FirestoreListAdapter

class FirestoreActivitiesAdapter(
    private val createRecord: (Int, Long) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : FirestoreListAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        delegateAdapters.get(viewType)
            .onCreateViewHolder(parent, createRecord, navigateToDetails)
}
