package com.jdevs.timeo.ui.overview

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.OverviewFragBinding
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.common.adapter.ListAdapter
import com.jdevs.timeo.ui.projects.ProjectDelegateAdapter
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.setupAdapter
import kotlinx.android.synthetic.main.overview_frag.activities_list
import kotlinx.android.synthetic.main.overview_frag.projects_list
import javax.inject.Inject

class OverviewFragment : Fragment() {

    private val projectsAdapter by lazy { ListAdapter(ProjectDelegateAdapter()) }
    private val activitiesAdapter by lazy { ListAdapter(ActivityDelegateAdapter()) }

    @Inject
    lateinit var viewModel: OverviewViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = OverviewFragBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        projects_list.setupAdapter(projectsAdapter)

        viewModel.topProjects.observe(viewLifecycleOwner) { list ->

            projectsAdapter.submitList(list)
            viewModel.setProjectsSize(list.size)
        }

        observeEvent(viewModel.navigateToProjects) {

            findNavController().navigateAnimated(R.id.projects_fragment_dest)
        }

        viewModel.activitiesEnabled.observe(viewLifecycleOwner) { isEnabled ->

            if (isEnabled) {

                activities_list.setupAdapter(activitiesAdapter)

                viewModel.topActivities.observe(viewLifecycleOwner) { list ->

                    activitiesAdapter.submitList(list)
                    viewModel.setActivitiesSize(list.size)
                }

                observeEvent(viewModel.navigateToActivities) {

                    findNavController().navigateAnimated(R.id.activities_fragment_dest)
                }
            } else {

                viewModel.activitiesEnabled.removeObservers(viewLifecycleOwner)
                viewModel.navigateToActivities.removeObservers(viewLifecycleOwner)
            }
        }
    }
}
