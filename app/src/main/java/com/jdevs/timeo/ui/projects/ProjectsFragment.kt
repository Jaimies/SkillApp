package com.jdevs.timeo.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R.id.addproject_fragment_dest
import com.jdevs.timeo.R.menu.projects_frag_menu
import com.jdevs.timeo.R.string.todo
import com.jdevs.timeo.databinding.ProjectsFragBinding
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.projects.ProjectsFragmentDirections.Companion.actionToProjectDetailFragment
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.projects_frag.recycler_view

@AndroidEntryPoint
class ProjectsFragment : ListFragment<ProjectItem>(projects_frag_menu) {

    override val delegateAdapter by lazy {
        ProjectDelegateAdapter({ snackbar(todo) }, ::navigateToDetails)
    }

    override val viewModel: ProjectsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding =
            ProjectsFragBinding.inflate(inflater, container, false).also {
                it.lifecycleOwner = viewLifecycleOwner
                it.viewModel = viewModel
            }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setup()

        observe(viewModel.navigateToAddActivity) {
            findNavController().navigateAnimated(addproject_fragment_dest)
        }
    }

    private fun navigateToDetails(index: Int) {
        val directions = actionToProjectDetailFragment(getItem(index))
        findNavController().navigate(directions)
    }
}
