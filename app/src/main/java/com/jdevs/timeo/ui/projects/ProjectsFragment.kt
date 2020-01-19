package com.jdevs.timeo.ui.projects


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProjectsFragBinding
import com.jdevs.timeo.domain.model.Project
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.ProjectsConstants
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.showSnackbar
import javax.inject.Inject

class ProjectsFragment : ListFragment<Project>() {

    override val adapter by lazy {
        PagingAdapter(
            ProjectDelegateAdapter(), { _, _ -> showSnackbar(R.string.todo) }, ::navigateToDetails
        )
    }

    override val firestoreAdapter by lazy {
        FirestoreListAdapter({ _, _ -> showSnackbar(R.string.todo) }, ::navigateToDetails)
    }

    override val menuId = R.menu.projects_fragment_menu

    @Inject
    override lateinit var viewModel: ProjectsViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        ProjectsFragBinding.inflate(inflater, container, false).let {

            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.recyclerView.setup(ProjectsConstants.VISIBLE_THRESHOLD)

            observeEvent(viewModel.navigateToAddActivity) {
                findNavController().navigate(R.id.addproject_fragment_dest)
            }

            return it.root
        }
    }

    private fun navigateToDetails(index: Int) {

        val directions = ProjectsFragmentDirections
            .actionProjectsFragmentToProjectDetailFragment(getItem(index))

        findNavController().navigate(directions)
    }
}