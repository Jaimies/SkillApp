package com.jdevs.timeo.ui.projects

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectsItemBinding
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.recordable.RecordableViewHolder
import com.jdevs.timeo.util.ui.inflateDataBinding

class ProjectDelegateAdapter(
    private val showRecordDialog: () -> Unit,
    private val navigateToDetails: (index: Int) -> Unit
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        parent.inflateDataBinding<ProjectsItemBinding>(R.layout.projects_item).run {
            val viewModel = ProjectViewModel().also { viewModel = it }
            return RecordableViewHolder(root, viewModel, { showRecordDialog() }, navigateToDetails)
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {
        holder as RecordableViewHolder<ProjectItem>
        holder.setItem(item as ProjectItem)
    }
}
