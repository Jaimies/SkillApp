package com.jdevs.timeo.ui.summary


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.common.adapter.ListAdapter
import com.jdevs.timeo.databinding.SummaryFragBinding
import com.jdevs.timeo.model.Activity
import com.jdevs.timeo.model.Project
import com.jdevs.timeo.ui.activities.ActivityDelegateAdapter
import com.jdevs.timeo.ui.projects.ProjectDelegateAdapter
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.setupAdapter
import javax.inject.Inject

class SummaryFragment : Fragment() {

    @Inject
    lateinit var viewModel: SummaryViewModel

    private val projectsAdapter by lazy { ListAdapter<Project>(ProjectDelegateAdapter()) }
    private val activitiesAdapter by lazy { ListAdapter<Activity>(ActivityDelegateAdapter()) }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SummaryFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel

            it.projectsList.setupAdapter(projectsAdapter)
            it.activitiesList.setupAdapter(activitiesAdapter)
        }

        viewModel.run {

            observeEvent(navigateToProjects) {

                findNavController().navigate(R.id.action_summaryFragment_to_projectsGraph)
            }

            observeEvent(navigateToActivities) {

                findNavController().navigate(R.id.action_summaryFragment_to_activitiesGraph)
            }

            topProjects.observe(viewLifecycleOwner) { list ->

                projectsAdapter.setItems(list)
                setProjectsSize(list.size)
            }

            topActivities.observe(viewLifecycleOwner) { list ->

                activitiesAdapter.setItems(list)
                setActivitiesSize(list.size)
            }
        }

        return binding.root
    }
}
