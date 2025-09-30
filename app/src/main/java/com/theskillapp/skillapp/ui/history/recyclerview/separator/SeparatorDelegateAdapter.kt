package com.theskillapp.skillapp.ui.history.recyclerview.separator

import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.SeparatorItemBinding
import com.theskillapp.skillapp.model.HistoryUiModel.Separator
import com.theskillapp.skillapp.shared.recyclerview.adapter.DelegateAdapter
import com.theskillapp.skillapp.shared.extensions.inflateDataBinding
import com.theskillapp.skillapp.shared.time.DateFormatter
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import javax.inject.Inject

class SeparatorDelegateAdapter @Inject constructor(
    private val viewHolderFactory: ViewHolder.Factory,
) : DelegateAdapter<Separator, SeparatorDelegateAdapter.ViewHolder> {
    override fun onCreateViewHolder(parent: ViewGroup, lifecycleOwner: LifecycleOwner): ViewHolder {
        return viewHolderFactory.create(parent.inflateDataBinding(R.layout.separator_item))
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Separator) {
        holder.setData(item)
    }

    class ViewHolder @AssistedInject constructor(
        @Assisted
        private val binding: SeparatorItemBinding,
        private val dateFormatter: DateFormatter,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun setData(separator: Separator) {
            binding.separator = separator
            binding.dateFormatter = dateFormatter
        }

        @AssistedFactory
        interface Factory {
            fun create(binding: SeparatorItemBinding): ViewHolder
        }
    }
}
