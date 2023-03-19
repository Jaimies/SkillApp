package com.maxpoliakov.skillapp.ui.history.recyclerview.separator

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SeparatorItemBinding
import com.maxpoliakov.skillapp.model.HistoryUiModel.Separator
import com.maxpoliakov.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.shared.extensions.inflateDataBinding

class SeparatorDelegateAdapter : DelegateAdapter<Separator, SeparatorDelegateAdapter.ViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): ViewHolder {
        return ViewHolder(parent.inflateDataBinding(R.layout.separator_item))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Separator) {
        holder.setData(item)
    }

    class ViewHolder(
        private val binding: SeparatorItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(separator: Separator) {
            binding.separator = separator
        }
    }
}