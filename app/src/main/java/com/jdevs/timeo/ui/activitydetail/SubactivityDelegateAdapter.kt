package com.jdevs.timeo.ui.activitydetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SubactivitiesItemBinding
import com.jdevs.timeo.model.ActivityMinimalItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.inflateDataBinding

class SubactivityDelegateAdapter(
    private val showRecordDialog: (index: Int) -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): ViewHolder {

        parent.inflateDataBinding<SubactivitiesItemBinding>(R.layout.subactivities_item).run {

            val viewModel = SubActivityViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setActivity(item as ActivityMinimalItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: SubActivityViewModel,
        private val showRecordDialog: (index: Int) -> Unit,
        private val navigateToDetail: (index: Int) -> Unit
    ) : BaseViewHolder(view) {

        init {
            viewModel.navigateToDetails.observe { navigateToDetail(adapterPosition) }
            viewModel.showRecordDialog.observe { showRecordDialog(adapterPosition) }
        }

        fun setActivity(activity: ActivityMinimalItem) = viewModel.setData(activity)
    }
}
