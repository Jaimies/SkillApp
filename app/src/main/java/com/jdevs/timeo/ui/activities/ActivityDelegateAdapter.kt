package com.jdevs.timeo.ui.activities

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.inflateDataBinding

class ActivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        parent.inflateDataBinding<ActivitiesItemBinding>(R.layout.activities_item).run {

            val viewModel = ActivityViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setActivity(item as ActivityItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: ActivityViewModel,
        private val showRecordDialog: (index: Int) -> Unit,
        private val navigateToDetail: (index: Int) -> Unit
    ) : BaseViewHolder(view) {

        init {
            viewModel.navigateToDetails.observe { navigateToDetail(adapterPosition) }
            viewModel.showRecordDialog.observe { showRecordDialog(adapterPosition) }
        }

        fun setActivity(activity: ActivityItem) = viewModel.setData(activity)
    }
}
