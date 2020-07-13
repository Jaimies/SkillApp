package com.jdevs.timeo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.jdevs.timeo.databinding.TasksFragBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.ui.common.ListFragment
import kotlinx.android.synthetic.main.tasks_frag.tasks_list

class TasksFragment : ListFragment<TaskItem>() {

    override val viewModel: TasksViewModel by viewModels()
    override val delegateAdapter by lazy { TaskDelegateAdapter(::setTaskCompleted) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = TasksFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tasks_list.setup(PAGE_SIZE)
    }

    private fun setTaskCompleted(position: Int, isCompleted: Boolean) {
        viewModel.setTaskCompleted(getItem(position).id, isCompleted)
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}
