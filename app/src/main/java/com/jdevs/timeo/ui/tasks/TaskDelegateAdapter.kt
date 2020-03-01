package com.jdevs.timeo.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.databinding.TasksItemBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.view.fragmentActivity

class TaskDelegateAdapter(private val setTaskCompleted: (Int, Boolean) -> Unit = { _, _ -> }) :
    DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val viewModel = TaskViewModel()

        val binding = TasksItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = parent.fragmentActivity
        }

        return ViewHolder(binding.root, viewModel, setTaskCompleted)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ViewItem) {

        holder as ViewHolder
        holder.setTask(item as TaskItem)
    }

    class ViewHolder(
        view: View,
        private val viewModel: TaskViewModel,
        setTaskCompleted: (Int, Boolean) -> Unit
    ) : BaseViewHolder(view) {

        init {

            viewModel.isCompleted.observe { isCompleted ->

                setTaskCompleted(adapterPosition, isCompleted)
            }
        }

        fun setTask(task: TaskItem) = viewModel.setTask(task)
    }
}
