package com.jdevs.timeo.ui.projects

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectsItemBinding
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.inflateDataBinding

class ProjectDelegateAdapter(
    private val showRecordDialog: () -> Unit = {},
    private val navigateToDetails: (Int) -> Unit = {}
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        parent.inflateDataBinding<ProjectsItemBinding>(R.layout.projects_item).run {
            val viewModel = ProjectViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel, showRecordDialog, navigateToDetails)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setProject(item as ProjectItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: ProjectViewModel,
        private val showRecordDialog: () -> Unit,
        private val navigateToDetails: (Int) -> Unit
    ) : BaseViewHolder(view) {

        init {

            viewModel.navigateToDetails.observe { navigateToDetails(adapterPosition) }
            viewModel.showRecordDialog.observe { showRecordDialog() }
        }

        fun setProject(project: ProjectItem) = viewModel.setProject(project)
    }
}
