package com.jdevs.timeo.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.common.adapter.DelegateAdapter
import com.jdevs.timeo.common.adapter.PagingAdapter
import com.jdevs.timeo.common.adapter.ViewItem
import com.jdevs.timeo.common.adapter.createViewModel
import com.jdevs.timeo.databinding.ProjectsItemBinding
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.util.extensions.getFragmentActivity

class ProjectDelegateAdapter : DelegateAdapter {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        createRecord: (Int, Long) -> Unit,
        navigateToDetails: (Int) -> Unit,
        showDeleteDialog: (Int) -> Unit
    ): RecyclerView.ViewHolder {

        val fragmentActivity = parent.getFragmentActivity()
        val inflater = LayoutInflater.from(fragmentActivity)
        val viewModel = createViewModel(fragmentActivity, ProjectViewModel::class)

        val binding = ProjectsItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, createRecord, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setProject(item as Project)
    }

    class ViewHolder(
        rootView: View,
        private val viewModel: ProjectViewModel,
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

        fun setProject(project: Project) = viewModel.setProject(project)
    }
}
