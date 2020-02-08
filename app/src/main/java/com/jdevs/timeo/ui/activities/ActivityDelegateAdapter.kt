package com.jdevs.timeo.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.ui.common.adapter.createViewModel
import com.jdevs.timeo.util.extensions.fragmentActivity

class ActivityDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        navigateToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.fragmentActivity
        val viewModel = createViewModel(fragmentActivity, ActivityViewModel::class)

        val binding = ActivitiesItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, createRecord, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setActivity(item as ActivityItem)
    }

    class ViewHolder(
        rootView: View,
        private val viewModel: ActivityViewModel,
        private val createRecord: (Int, Long) -> Unit = { _, _ -> },
        private val navigateToDetails: (Int) -> Unit = {}
    ) : PagingAdapter.ViewHolder(rootView) {

        init {

            viewModel.apply {

                navigateToDetails.observeEvent(lifecycleOwner) { navigateToDetails(adapterPosition) }
                showRecordDialog.observeEvent(lifecycleOwner) {
                    RecordDialog(context) { time -> createRecord(adapterPosition, time) }.show()
                }
            }
        }

        fun setActivity(activity: ActivityItem) = viewModel.setActivity(activity)
    }
}
