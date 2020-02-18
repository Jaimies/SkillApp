package com.jdevs.timeo.ui.tasks

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.databinding.TasksFragBinding
import com.jdevs.timeo.model.TaskItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.appComponent
import javax.inject.Inject

class TasksFragment : ListFragment<TaskItem>() {

    @Inject
    override lateinit var viewModel: TasksViewModel

    override val adapter by lazy { PagingAdapter(TaskDelegateAdapter()) }
    override val firestoreAdapter by lazy { FirestoreListAdapter() }
    override val menuId = -1

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

    companion object {
        private const val PAGE_SIZE = 20
    }
}
