package com.maxpoliakov.skillapp.ui.activitydetail

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SubactivitiesItemBinding
import com.maxpoliakov.skillapp.model.Recordable
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewHolder
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

typealias ViewHolder = RecordableViewHolder<Recordable>

class SubactivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter<Recordable, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<SubactivitiesItemBinding>(R.layout.subactivities_item).run {
            val viewModel = SubActivityViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: Recordable) {
        holder.setItem(item)
    }
}
