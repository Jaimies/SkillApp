package com.jdevs.timeo.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ViewItem

interface DelegateAdapter<T: ViewItem, VH: RecyclerView.ViewHolder> {
    fun onCreateViewHolder(parent: ViewGroup): VH
    fun onBindViewHolder(holder: VH, item: T)
}
