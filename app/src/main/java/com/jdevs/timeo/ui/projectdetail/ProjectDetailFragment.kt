package com.jdevs.timeo.ui.projectdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectdetailFragBinding
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.ui.tasks.AddTaskFragment
import com.jdevs.timeo.ui.tasks.TaskDelegateAdapter
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.lifecycle.viewModels
import com.jdevs.timeo.util.ui.navigateAnimated
import com.jdevs.timeo.util.ui.setupAdapter
import kotlinx.android.synthetic.main.tasks_frag.tasks_list
import javax.inject.Inject

class ProjectDetailFragment : ActionBarFragment() {

    private val args: ProjectDetailFragmentArgs by navArgs()
    private val adapter by lazy { ListAdapter(TaskDelegateAdapter(viewModel::setTaskCompleted)) }
    override val menuId = R.menu.activitydetail_frag_menu

    private val viewModel by viewModels { viewModelFactory.create(args.project.id) }

    @Inject
    lateinit var viewModelFactory: ProjectDetailViewModel.Factory

    override fun onAttach(context: Context) {
        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProjectdetailFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        tasks_list.setupAdapter(adapter)

        observe(viewModel.topTasks) { tasks -> adapter.submitList(tasks) }

        observe(viewModel.goToTasks) {
            findNavController().navigateAnimated(R.id.tasks_fragment_dest)
        }

        observe(viewModel.addTask) {
            AddTaskFragment.create(requireActivity().supportFragmentManager, args.project.id)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {

            val directions = ProjectDetailFragmentDirections
                .actionProjectDetailFragmentToAddProjectFragment(viewModel.project.value!!)

            findNavController().navigate(directions)
            return true
        }

        return false
    }
}
