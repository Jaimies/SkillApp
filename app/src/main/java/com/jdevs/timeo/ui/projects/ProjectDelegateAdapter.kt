package com.jdevs.timeo.ui.projects

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.databinding.ProjectsItemBinding
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.createViewModel
import com.jdevs.timeo.util.fragmentActivity

class ProjectDelegateAdapter(
    private val showRecordDialog: () -> Unit = {},
    private val navigateToDetails: (Int) -> Unit = {}
) : DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.fragmentActivity
        val viewModel = createViewModel(fragmentActivity, ProjectViewModel::class)

        val binding = ProjectsItemBinding.inflate(inflater, parent, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = fragmentActivity

        return ViewHolder(binding.root, viewModel, showRecordDialog, navigateToDetails)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setProject(item as ProjectItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: ProjectViewModel,
        private val showRecordDialog: () -> Unit = {},
        private val navigateToDetails: (Int) -> Unit = {}
    ) : BaseViewHolder(view) {

        init {

            viewModel.navigateToDetails.observe { navigateToDetails(adapterPosition) }
            viewModel.showRecordDialog.observe { showRecordDialog() }
        }

        fun setProject(project: ProjectItem) = viewModel.setProject(project)
    }
}
