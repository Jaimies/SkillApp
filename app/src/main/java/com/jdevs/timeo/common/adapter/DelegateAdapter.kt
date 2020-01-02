package com.jdevs.timeo.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit = { _, _ -> },
        goToDetails: (Int) -> Unit = {},
        showDeleteDialog: (Int) -> Unit = {}
    ): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem)
}
