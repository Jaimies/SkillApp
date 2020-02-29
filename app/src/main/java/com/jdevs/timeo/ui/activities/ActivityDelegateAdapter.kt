package com.jdevs.timeo.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.createViewModel
import com.jdevs.timeo.util.fragmentActivity

class ActivityDelegateAdapter(
    private val showRecordDialog: (Int) -> Unit = {},
    private val navigateToDetails: (Int) -> Unit = {}
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.fragmentActivity
        val viewModel = createViewModel(fragmentActivity, ActivityViewModel::class)

        val binding = ActivitiesItemBinding.inflate(inflater, parent, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = fragmentActivity

        return ViewHolder(binding.root, viewModel, showRecordDialog, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setActivity(item as ActivityItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: ActivityViewModel,
        private val showRecordDialog: (Int) -> Unit = { },
        private val navigateToDetail: (Int) -> Unit = {}
    ) : BaseViewHolder(view) {

        init {

            viewModel.navigateToDetails.observe { navigateToDetail(adapterPosition) }
            viewModel.showRecordDialog.observe { showRecordDialog(adapterPosition) }
        }

        fun setActivity(activity: ActivityItem) = viewModel.setActivity(activity)
    }
}
