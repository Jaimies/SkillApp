package com.maxpoliakov.skillapp.shared.recyclerview

import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder

abstract class BaseViewHolder(private val binding: ViewDataBinding) : ViewHolder(binding.root) {
    protected val context: Context get() = binding.root.context
    protected val lifecycleOwner get() = requireNotNull(binding.lifecycleOwner)
}
