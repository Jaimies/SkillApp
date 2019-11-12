package com.jdevs.timeo.models

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.HomeFragmentDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.TaskListFragmentDirections
import com.jdevs.timeo.adapters.ActivitiesListAdapter
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.viewmodels.ActivitiesListViewModel

open class ActivitiesListFragment : ActionBarFragment() {

    private val mActivities = ArrayList<TimeoActivity>()
    private val mItemIds = ArrayList<String>()

    private lateinit var mLoader: FrameLayout
    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: Button
    private lateinit var mRecyclerView: RecyclerView

    lateinit var mViewAdapter: ActivitiesListAdapter

    private var activitiesListViewModel: ActivitiesListViewModel? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitiesListViewModel =
            ViewModelProviders.of(this).get(ActivitiesListViewModel::class.java)
    }

    override fun onStart() {
        super.onStart()

        mActivities.clear()
        mItemIds.clear()
    }

    fun setupActivityListener(
        recyclerView: RecyclerView,
        loaderLayout: FrameLayout,
        createNewActivityView: LinearLayout,
        createNewActivityButton: Button
    ) {

        mLoader = loaderLayout.apply {

            visibility = View.VISIBLE
        }

        mCreateNewActivityView = createNewActivityView
        mCreateNewActivityButton = createNewActivityButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_showCreateActivityFragment)
            }
        }

        setupRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(RealtimeScrollListener(::getActivities))

        getActivities()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter =
            ActivitiesListAdapter(
                mActivities,
                ::navigateToDetails,
                ::createRecord
            )

        mRecyclerView = recyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter
        }
    }

    private fun getActivities() {
        val activitiesListLiveData = activitiesListViewModel?.getActivitiesListLiveData() ?: return

        activitiesListLiveData.observe(this) { operation ->

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
                    mLoader.visibility = View.GONE
                }
            }

            if (mActivities.isEmpty()) {

                if (mCreateNewActivityView.visibility != View.VISIBLE) {
                    mCreateNewActivityView.visibility = View.VISIBLE
                }
            } else {

                if (mCreateNewActivityView.visibility != View.GONE) {
                    mCreateNewActivityView.visibility = View.GONE
                }
            }
        }
    }

    private fun addActivity(activity: TimeoActivity, id: String) {
        if (mItemIds.contains(id)) {

            return
        }

        mActivities.add(activity)
        mItemIds.add(id)
        mViewAdapter.notifyItemInserted(mActivities.size - 1)
    }

    private fun removeActivity(id: String) {

        val index = mActivities.withIndex().filterIndexed { index, _ -> mItemIds[index] == id }
            .map { it.index }
            .first()

        mActivities.removeAt(index)

        mViewAdapter.notifyItemRemoved(index)
    }

    private fun modifyActivity(activity: TimeoActivity, id: String) {
        val index =
            mActivities.withIndex().filterIndexed { index, _ -> mItemIds[index] == id }
                .map { it.index }
                .first()

        mActivities[index] = activity

        mViewAdapter.notifyItemChanged(index)
    }

    private fun navigateToDetails(index: Int) {

        val activityId = mItemIds[index]

        try {

            val action = HomeFragmentDirections
                .actionShowActivityDetails(mActivities[index], activityId)

            findNavController().navigate(action)
        } catch (e: IllegalArgumentException) {

            val action = TaskListFragmentDirections
                .actionShowActivityDetails(mActivities[index], activityId)

            findNavController().navigate(action)
        }
    }

    private fun createRecord(index: Int, time: Int) {
        activitiesListViewModel?.createRecord(mActivities[index].title, time, mItemIds[index])
    }
}
