package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.adapters.ActivitiesListAdapter
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.ScrollDownListener
import com.jdevs.timeo.viewmodels.ActivitiesListViewModel
import kotlinx.android.synthetic.main.activities_list.view.activitiesRecyclerView
import kotlinx.android.synthetic.main.activities_list.view.createNewActivityButton
import kotlinx.android.synthetic.main.activities_list.view.createNewActivityView
import kotlinx.android.synthetic.main.activities_list.view.listLoader

class ActivityListFragment : ActionBarFragment() {

    private lateinit var mLoader: FrameLayout
    private lateinit var mActivitiesRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: Button
    private lateinit var mRecyclerView: RecyclerView

    private val activityList = ArrayList<TimeoActivity>()
    private val idList = ArrayList<String>()

    private val mViewAdapter: ActivitiesListAdapter by lazy {
        ActivitiesListAdapter(activityList, ::createRecord, ::navigateToDetails)
    }

    private val activitiesListViewModel: ActivitiesListViewModel? by lazy {
        ViewModelProviders.of(this).get(ActivitiesListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_activity_list, container, false)

        view.apply {

            mLoader = listLoader as FrameLayout

            mActivitiesRecyclerView = activitiesRecyclerView

            mCreateNewActivityView = createNewActivityView
            mCreateNewActivityButton = createNewActivityButton

            mRecyclerView = activitiesRecyclerView
        }

        mLoader.apply {
            visibility = View.VISIBLE
        }

        mCreateNewActivityButton.setOnClickListener {
            findNavController().navigate(R.id.action_showCreateActivityFragment)
        }

        val linearLayoutManager = LinearLayoutManager(context)

        mRecyclerView.apply {

            layoutManager = linearLayoutManager
            adapter = mViewAdapter
        }

        mRecyclerView.addOnScrollListener(ScrollDownListener(::getActivities))

        getActivities()

        return view
    }

    override fun onStart() {
        super.onStart()

        activityList.clear()
        idList.clear()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        createOptionsMenu(menu, inflater, R.menu.action_bar_activity_list)
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

            if (activityList.isEmpty()) {

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
        if (idList.contains(id)) {

            return
        }

        activityList.add(activity)
        idList.add(id)
        mViewAdapter.notifyItemInserted(activityList.size - 1)
    }

    private fun removeActivity(id: String) {

        val index = activityList.withIndex().filterIndexed { index, _ -> idList[index] == id }
            .map { it.index }
            .first()

        activityList.removeAt(index)
        idList.removeAt(index)

        mViewAdapter.notifyItemRemoved(index)
    }

    private fun modifyActivity(activity: TimeoActivity, id: String) {
        val index =
            activityList.withIndex().filterIndexed { index, _ -> idList[index] == id }
                .map { it.index }
                .first()

        activityList[index] = activity

        mViewAdapter.notifyItemChanged(index)
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

    private fun createRecord(index: Int, time: Int) {
        activitiesListViewModel?.createRecord(activityList[index].name, time, idList[index])
    }
}
