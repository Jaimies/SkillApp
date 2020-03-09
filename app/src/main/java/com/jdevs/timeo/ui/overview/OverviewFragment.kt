package com.jdevs.timeo.ui.overview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.OverviewDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.OverviewFragBinding
import com.jdevs.timeo.di.ViewModelFactory
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.ui.projects.ProjectDelegateAdapter
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.view.setupAdapter
import kotlinx.android.synthetic.main.overview_frag.activities_list
import kotlinx.android.synthetic.main.overview_frag.projects_list
import javax.inject.Inject

class OverviewFragment : Fragment() {

    private val projectsAdapter by lazy {
        ListAdapter(ProjectDelegateAdapter(::showProjectRecordDialog, ::navigateToProjectDetail))
    }

    private val activitiesAdapter by lazy {
        ListAdapter(ActivityDelegateAdapter(::showActivityRecordDialog, ::navigateToActivityDetail))
    }

    private val viewModel: OverviewViewModel by viewModels { viewModelFactory }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = OverviewFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel.projects.observe(
            R.id.projects_fragment_dest,
            R.id.addproject_fragment_dest,
            projectsAdapter, projects_list
        )

        observe(viewModel.activitiesEnabled) { isEnabled ->

            if (isEnabled) {

                viewModel.activities.observe(
                    R.id.activities_fragment_dest,
                    R.id.addactivity_fragment_dest,
                    activitiesAdapter, activities_list
                )
            } else {

                viewModel.activities.navigateToAdd.removeObservers(viewLifecycleOwner)
                viewModel.activities.navigateToList.removeObservers(viewLifecycleOwner)
            }
        }
    }

    private fun showProjectRecordDialog() {

        showTimePicker { _, _ -> snackbar(R.string.todo) }
    }

    private fun showActivityRecordDialog(index: Int) {

        showTimePicker { hour, minute -> viewModel.createRecord(index, getMins(hour, minute)) }
    }

    private fun navigateToProjectDetail(index: Int) {

        val directions =
            OverviewDirections.actionToProjectDetailFragment(viewModel.projects.data.value!![index])
        findNavController().navigateAnimated(directions)
    }

    private fun navigateToActivityDetail(index: Int) {

        val directions =
            OverviewDirections.actionToActivityDetailFragment(viewModel.activities.data.value!![index])
        findNavController().navigateAnimated(directions)
    }

    private fun OverviewViewModel.DataWrapper<*>.observe(
        @IdRes listId: Int, @IdRes addId: Int, adapter: ListAdapter, recyclerView: RecyclerView
    ) {

        recyclerView.setupAdapter(adapter)

        observe(data) { list ->

            adapter.submitList(list)
            setSize(list.size)
        }

        observe(navigateToList) { findNavController().navigateAnimated(listId) }
        observe(navigateToAdd) { findNavController().navigateAnimated(addId) }
    }
}
