package com.maxpoliakov.skillapp.ui.common.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<T : Any, VH : RecyclerView.ViewHolder> {
    fun onCreateViewHolder(parent: ViewGroup): VH
    fun onBindViewHolder(holder: VH, item: T)
}
