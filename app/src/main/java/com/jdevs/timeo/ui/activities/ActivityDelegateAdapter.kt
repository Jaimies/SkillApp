package com.jdevs.timeo.ui.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.DelegateAdapter
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.model.Activity
import java.util.UUID

class ActivityDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        goToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.context as FragmentActivity

        val viewModel = ViewModelProvider(fragmentActivity)
            .get(UUID.randomUUID().toString(), ActivityViewModel::class.java)

        val binding = ActivitiesItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, createRecord, goToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.bindActivity(item as Activity)
    }

    class ViewHolder(
        rootView: View,
        private val viewModel: ActivityViewModel,
        private val createRecord: (Int, Long) -> Unit = { _, _ -> },
        private val navigateToDetails: (Int) -> Unit = {}
    ) : ListAdapter.ViewHolder(rootView) {

        init {

            viewModel.apply {

                navigateToDetails.observeEvent(lifecycleOwner) {

                    navigateToDetails(adapterPosition)
                }

                showRecordDialog.observeEvent(lifecycleOwner) {

                    RecordDialog(context) { time ->

                        createRecord(adapterPosition, time)
                    }.show()
                }
            }
        }

        fun bindActivity(activity: Activity) = viewModel.setActivity(activity)
    }
}
