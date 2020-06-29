package com.jdevs.timeo.ui.activities

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.recordable.RecordableViewHolder
import com.jdevs.timeo.util.ui.inflateDataBinding

class ActivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        parent.inflateDataBinding<ActivitiesItemBinding>(R.layout.activities_item).run {
            val viewModel = ActivityViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {
        holder as RecordableViewHolder<ActivityItem>
        holder.setItem(item as ActivityItem)
    }
}
