package com.jdevs.timeo.adapter.delegates

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface ViewTypeDelegateAdapter {

    fun onCreateViewHolder(
        parent: ViewGroup,
        record: (Int, Long) -> Unit = { _, _ -> }, goToDetails: (Int) -> Unit = {},
        deleteRecord: (Int) -> Unit = {}
    ): RecyclerView.ViewHolder

    fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewType)
}