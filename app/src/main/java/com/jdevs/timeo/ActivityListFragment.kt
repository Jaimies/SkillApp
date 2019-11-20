package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapters.ActivitiesListAdapter
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.FragmentActivityListBinding
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.ScrollDownListener
import com.jdevs.timeo.navigators.ActivityListNavigator
import com.jdevs.timeo.viewmodels.ActivitiesListViewModel

class ActivityListFragment : ActionBarFragment(),
    ActivityListNavigator {

    private lateinit var mRecyclerView: RecyclerView

    private val activityList = ArrayList<TimeoActivity>()
    private val idList = ArrayList<String>()

    private val mAdapter: ActivitiesListAdapter by lazy {
        ActivitiesListAdapter(activityList, ::createRecord, ::navigateToDetails)
    }

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ActivitiesListViewModel::class.java).also {
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
            }

        mRecyclerView = binding.activityRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
            addOnScrollListener(ScrollDownListener(::getActivities))
        }

        getActivities()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        activityList.clear()
        idList.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        createOptionsMenu(menu, inflater, R.menu.action_bar_activity_list)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.navigator = null
    }

    override fun createActivity() {
        findNavController().navigate(R.id.action_showCreateActivityFragment)
    }

    private fun getActivities() {
        val liveData = viewModel.activitiesListLiveData ?: return

        liveData.observe(this) { operation ->

            when (operation.type) {
                R.id.OPERATION_ADDED -> {
                    val activity = operation.activity ?: return@observe
                    addActivity(activity, operation.id)
                }

                R.id.OPERATION_MODIFIED -> {
                    val activity = operation.activity ?: return@observe
                    modifyActivity(activity, operation.id)
                }

                R.id.OPERATION_REMOVED -> {
                    removeActivity(operation.id)
                }

                R.id.OPERATION_LOADED -> {
                    viewModel.onLoaded()
                }
            }

            viewModel.setLength(activityList.size)
        }
    }

    private fun addActivity(activity: TimeoActivity, id: String) {
        if (idList.contains(id)) {

            return
        }

        activityList.add(activity)
        idList.add(id)
        mAdapter.notifyItemInserted(activityList.size - 1)
    }

    private fun removeActivity(id: String) {

        val index = activityList.withIndex().filterIndexed { index, _ -> idList[index] == id }
            .map { it.index }
            .first()

        activityList.removeAt(index)
        idList.removeAt(index)

        mAdapter.notifyItemRemoved(index)
    }

    private fun modifyActivity(activity: TimeoActivity, id: String) {
        val index =
            activityList.withIndex().filterIndexed { index, _ -> idList[index] == id }
                .map { it.index }
                .first()

        activityList[index] = activity

        mAdapter.notifyItemChanged(index)
    }

    private fun navigateToDetails(index: Int) {

        val activityId = idList[index]

        try {
            val action = OverviewFragmentDirections
                .actionShowActivityDetails(activityList[index], activityId)

            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {

            val action = ActivityListFragmentDirections.actionShowActivityDetails(
                activityList[index],
                activityId
            )

            findNavController().navigate(action)
        }
    }

    private fun createRecord(index: Int, time: Long) {
        viewModel.createRecord(activityList[index].name, time, idList[index])
    }
}
