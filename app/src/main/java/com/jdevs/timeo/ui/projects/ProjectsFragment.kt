package com.jdevs.timeo.ui.projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectsFragBinding
import com.jdevs.timeo.model.ProjectItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.projects_frag.recycler_view

private const val PROJECTS_VISIBLE_THRESHOLD = 10

@AndroidEntryPoint
class ProjectsFragment : ListFragment<ProjectItem>(R.menu.projects_frag_menu) {

    override val delegateAdapter by lazy {
        ProjectDelegateAdapter({ snackbar(R.string.todo) }, ::navigateToDetails)
    }

    override val viewModel: ProjectsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProjectsFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycler_view.setup(PROJECTS_VISIBLE_THRESHOLD)

        observe(viewModel.navigateToAddActivity) {
            findNavController().navigateAnimated(R.id.addproject_fragment_dest)
        }
    }

    private fun navigateToDetails(index: Int) {
        val directions =
            ProjectsFragmentDirections.actionProjectsFragmentToProjectDetailFragment(getItem(index))
        findNavController().navigate(directions)
    }
}
