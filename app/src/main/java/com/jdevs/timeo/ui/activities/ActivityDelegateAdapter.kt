package com.jdevs.timeo.ui.activities

import android.view.ViewGroup
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.recordable.RecordableViewHolder
import com.jdevs.timeo.util.ui.inflateDataBinding

typealias ViewHolder = RecordableViewHolder<ActivityItem>

class ActivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter<ActivityItem, ViewHolder> {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {
        parent.inflateDataBinding<ActivitiesItemBinding>(R.layout.activities_item).run {
            val viewModel = ActivityViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, item: ActivityItem) {
        holder.setItem(item)
    }
}
