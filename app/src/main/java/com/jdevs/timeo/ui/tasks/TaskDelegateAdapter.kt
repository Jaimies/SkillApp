package com.jdevs.timeo.ui.tasks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.databinding.TasksItemBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.model.ViewItem
import com.jdevs.timeo.ui.common.adapter.DelegateAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.createViewModel
import com.jdevs.timeo.util.fragmentActivity

class TaskDelegateAdapter(private val setTaskCompleted: (Int, Boolean) -> Unit = { _, _ -> }) :
    DelegateAdapter {

    override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val fragmentActivity = parent.fragmentActivity
        val viewModel = createViewModel(fragmentActivity, TaskViewModel::class)

        val binding = TasksItemBinding.inflate(inflater, parent, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = fragmentActivity
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
    ) : PagingAdapter.ViewHolder(view) {

        init {

            viewModel.isCompleted.observe(lifecycleOwner) { isCompleted ->

                setTaskCompleted(adapterPosition, isCompleted)
            }
        }

        fun setTask(task: TaskItem) = viewModel.setTask(task)
    }
}