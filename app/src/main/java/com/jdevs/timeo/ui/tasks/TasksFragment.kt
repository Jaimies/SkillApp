package com.jdevs.timeo.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.databinding.TasksFragBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.appComponent
import javax.inject.Inject

class TasksFragment : ListFragment<TaskItem>() {

    @Inject
    override lateinit var viewModel: TasksViewModel

    override val delegateAdapter by lazy { TaskDelegateAdapter(::setTaskCompleted) }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = TasksFragBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.tasksList.setup(PAGE_SIZE)

        return binding.root
    }

    private fun setTaskCompleted(position: Int, isCompleted: Boolean) {

        viewModel.setTaskCompleted(getItem(position).id, isCompleted)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}