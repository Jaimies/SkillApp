package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.common.InfiniteScrollListener
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.data.operations.ActivityOperation
import com.jdevs.timeo.databinding.FragmentActivityListBinding
import com.jdevs.timeo.ui.activities.adapter.ActivitiesAdapter
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.ui.overview.OverviewFragmentDirections

class ActivityListFragment : ActionBarFragment(),
    ActivityListViewModel.Navigator {

    override val menuId = R.menu.action_bar_activity_list
    private val mAdapter by lazy { ActivitiesAdapter(::createRecord, ::navigateToDetails) }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ActivityListViewModel::class.java).also {
            it.navigator = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentActivityListBinding.inflate(inflater, container, false).also {

                it.viewmodel = viewModel
                it.lifecycleOwner = this

                it.activityRecyclerView.apply {

                    layoutManager = LinearLayoutManager(context)
                    adapter = mAdapter

                    addOnScrollListener(InfiniteScrollListener(::getActivities))
                }
            }

        getActivities()

        return binding.root
    }

    override fun onDestroy() {

        super.onDestroy()
        viewModel.onFragmentDestroyed()
    }

    override fun createActivity() {
        findNavController().navigate(R.id.action_showCreateActivityFragment)
    }

    override fun onLastItemReached() {

        mAdapter.onLastItemReached()
    }

    private fun getActivities() {
        viewModel.activitiesLiveData?.observe(this) { operation ->

            operation as ActivityOperation

            when (operation.type) {
                R.id.OPERATION_ADDED -> {
                    val activity = operation.item ?: return@observe
                    mAdapter.addItem(activity, operation.id)
                }

                R.id.OPERATION_MODIFIED -> {
                    val activity = operation.item ?: return@observe
                    mAdapter.modifyItem(activity, operation.id)
                }

                R.id.OPERATION_REMOVED -> {
                    mAdapter.removeItem(operation.id)
                }

                R.id.OPERATION_LOADED -> {
                    viewModel.hideLoader()
                }
            }

            viewModel.setLength(mAdapter.dataItemCount)
        }
    }

    private fun navigateToDetails(index: Int) {

        val activityId = mAdapter.getId(index)
        val item = getActivity(index)

        try {
            val action = OverviewFragmentDirections.actionShowActivityDetails(item, activityId)

            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {

            val action =
                ActivityListFragmentDirections.actionShowActivityDetails(item, activityId)

            findNavController().navigate(action)
        }
    }

    private fun createRecord(index: Int, time: Long) {

        viewModel.createRecord(getActivity(index).name, time, mAdapter.getId(index))
    }

    private fun getActivity(index: Int) = mAdapter.getItem(index) as TimeoActivity
}
