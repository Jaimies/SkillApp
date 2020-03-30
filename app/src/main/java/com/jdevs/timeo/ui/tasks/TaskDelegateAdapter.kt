package com.jdevs.timeo.ui.tasks

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.TasksItemBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.BaseViewHolder
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.util.inflateDataBinding

class TaskDelegateAdapter(private val setTaskCompleted: (Int, Boolean) -> Unit = { _, _ -> }) :
    DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        parent.inflateDataBinding<TasksItemBinding>(R.layout.tasks_item).run {
            val viewModel = TaskViewModel().also { viewModel = it }
            return ViewHolder(root, viewModel, setTaskCompleted)
        }
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
