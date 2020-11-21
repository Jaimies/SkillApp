package com.jdevs.timeo.ui.activitydetail

import android.view.ViewGroup
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SubactivitiesItemBinding
import com.jdevs.timeo.model.Recordable
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.recordable.RecordableViewHolder
import com.jdevs.timeo.util.ui.inflateDataBinding

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
