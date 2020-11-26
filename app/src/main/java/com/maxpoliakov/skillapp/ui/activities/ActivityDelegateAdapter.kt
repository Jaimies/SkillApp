package com.maxpoliakov.skillapp.ui.activities

import android.view.ViewGroup
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.ActivitiesItemBinding
import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.ui.common.adapter.DelegateAdapter
import com.maxpoliakov.skillapp.ui.common.recordable.RecordableViewHolder
import com.maxpoliakov.skillapp.util.ui.inflateDataBinding

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
