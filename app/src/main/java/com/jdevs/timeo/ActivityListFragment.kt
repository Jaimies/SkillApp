package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.adapter.ActivitiesAdapter
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.FragmentActivityListBinding
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.ScrollDownListener
import com.jdevs.timeo.viewmodel.ActivityListViewModel

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

                    addOnScrollListener(ScrollDownListener(::getActivities))
                }
            }

        getActivities()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter.removeAllItems()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }

    override fun createActivity() {
        findNavController().navigate(R.id.action_showCreateActivityFragment)
    }

    override fun onLastItemReached() {

        mAdapter.onLastItemReached()
    }

    private fun getActivities() {
        val liveData = viewModel.activityListLiveData ?: return

        liveData.observe(this) { operation ->

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
                    viewModel.onLoaded()
                }
            }

            viewModel.setLength(mAdapter.itemCount)
        }
    }

    private fun navigateToDetails(index: Int) {

        val activityId = mAdapter.getId(index)
        val item = getItem(index)

        try {
            val action = OverviewFragmentDirections
                .actionShowActivityDetails(item, activityId)

            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {

            val action = ActivityListFragmentDirections
                .actionShowActivityDetails(item, activityId)

            findNavController().navigate(action)
        }
    }

    private fun createRecord(index: Int, time: Long) {
        viewModel.createRecord(getItem(index).name, time, mAdapter.getId(index))
    }

    private fun getItem(index: Int): TimeoActivity = mAdapter.getItem(index)
}
