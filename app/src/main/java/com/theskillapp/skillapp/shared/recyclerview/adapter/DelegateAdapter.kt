package com.theskillapp.skillapp.shared.recyclerview.adapter

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView

interface DelegateAdapter<T : Any, VH : RecyclerView.ViewHolder> {
    fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): VH
    fun onBindViewHolder(holder: VH, item: T)
}
