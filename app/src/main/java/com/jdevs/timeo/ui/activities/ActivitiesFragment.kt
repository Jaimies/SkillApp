package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.databinding.ActivitiesFragBinding
import com.jdevs.timeo.ui.activities.adapter.ActivitiesAdapter
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.observeEvent

class ActivitiesFragment : ListFragment<Activity>() {

    override val menuId = R.menu.activities_fragment_menu
    override val mAdapter by lazy { ActivitiesAdapter(::createRecord, ::navigateToDetails) }
    override val viewModel: ActivityListViewModel by viewModels()

    private lateinit var menu: Menu
    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = ActivitiesFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.recyclerView.setup(ActivitiesConstants.VISIBLE_THRESHOLD)
        }

        viewModel.apply {

            isLoading.observe(viewLifecycleOwner) { isLoading ->

                if (!isLoading) {

                    onItemsLoaded()
                }
            }

            observeEvent(navigateToAddEdit) {

                findNavController().navigate(R.id.action_activitiesFragment_to_addEditActivityFragment)
            }
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        this.menu = menu

        if (!isLoaded) {

            menu.forEach {

                it.isEnabled = false
            }
        }

        super.onPrepareOptionsMenu(menu)
    }

    private fun onItemsLoaded() {

        menu.forEach {

            it.isEnabled = true
        }

        isLoaded = true
    }

    private fun navigateToDetails(index: Int) {

        val id = mAdapter.getId(index)
        val activity = getActivity(index)

        val action = ActivitiesFragmentDirections
            .actionActivitiesFragmentToActivityDetailsFragment(activity = activity, id = id)

        findNavController().navigate(action)
    }

    private fun createRecord(index: Int, time: Long) {

        viewModel.createRecord(getActivity(index).name, time, mAdapter.getId(index))
    }

    private fun getActivity(index: Int) = mAdapter.getItem(index) as Activity
}
