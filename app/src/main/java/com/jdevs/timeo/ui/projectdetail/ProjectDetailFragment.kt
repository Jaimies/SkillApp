package com.jdevs.timeo.ui.projectdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectdetailFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.ui.tasks.TaskDelegateAdapter
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.contentView
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.setupAdapter
import com.jdevs.timeo.util.showKeyboard
import com.jdevs.timeo.util.window
import kotlinx.android.synthetic.main.add_task_layout.view.name_edit_text
import javax.inject.Inject

class ProjectDetailFragment : ActionBarFragment() {

    private val args: ProjectDetailFragmentArgs by navArgs()
    private val adapter by lazy { ListAdapter(TaskDelegateAdapter(viewModel::setTaskCompleted)) }
    override val menuId = R.menu.activity_detail_fragment_menu

    private val addTaskLayout by lazy {

        window.findViewById(R.id.add_task_layout) ?: layoutInflater.inflate(
            R.layout.add_task_layout,
            contentView, false
        ).also { contentView.addView(it) }
    }

    @Inject
    lateinit var viewModel: ProjectDetailViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setupProjectLiveData(args.project)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProjectdetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel
        }

        binding.tasksList.setupAdapter(adapter)

        viewModel.topTasks.observe(viewLifecycleOwner) { newItems ->
            adapter.submitList(newItems)
        }

        viewModel.project.observe(viewLifecycleOwner, viewModel::setProject)
        observeEvent(viewModel.goToTasks) {
            findNavController().navigate(R.id.tasks_fragment_dest)
        }

        observeEvent(viewModel.addTask) {

            addTaskLayout.run {

                visibility = VISIBLE
                name_edit_text.requestFocus()
                name_edit_text.showKeyboard()

                setOnClickListener {

                    visibility = GONE
                    hideKeyboard()
                }
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {

            val directions = ProjectDetailFragmentDirections
                .actionProjectDetailFragmentToAddProjectFragment(viewModel.project.value)

            findNavController().navigate(directions)
            return true
        }

        return false
    }
}
