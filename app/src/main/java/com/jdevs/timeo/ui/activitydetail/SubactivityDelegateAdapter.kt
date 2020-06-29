package com.jdevs.timeo.ui.activitydetail

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SubactivitiesItemBinding
import com.jdevs.timeo.model.ActivityMinimalItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.recordable.RecordableViewHolder
import com.jdevs.timeo.util.ui.inflateDataBinding

class SubactivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        parent.inflateDataBinding<SubactivitiesItemBinding>(R.layout.subactivities_item).run {
            val viewModel = SubActivityViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {
        holder as RecordableViewHolder<ActivityMinimalItem>
        holder.setItem(item as ActivityMinimalItem)
    }
}
