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
import com.jdevs.timeo.models.RealtimeScrollListener
import com.jdevs.timeo.viewmodels.ActivitiesListViewModel
import kotlinx.android.synthetic.main.partial_activities_list.view.activitiesRecyclerView
import kotlinx.android.synthetic.main.partial_activities_list.view.createNewActivityButton
import kotlinx.android.synthetic.main.partial_activities_list.view.createNewActivityView
import kotlinx.android.synthetic.main.partial_activities_list.view.listLoader

class ActivitiesListFragment : ActionBarFragment() {

    private lateinit var mLoader: FrameLayout

    private lateinit var mActivitiesRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: Button

    private val mActivities = ArrayList<TimeoActivity>()
    private val mItemIds = ArrayList<String>()
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewAdapter: ActivitiesListAdapter

    private var activitiesListViewModel: ActivitiesListViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitiesListViewModel =
            ViewModelProviders.of(this).get(ActivitiesListViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        view.apply {

            mLoader = listLoader as FrameLayout

            mActivitiesRecyclerView = activitiesRecyclerView

            mCreateNewActivityView = createNewActivityView
            mCreateNewActivityButton = createNewActivityButton

            mRecyclerView = activitiesRecyclerView
        }

        setupActivityListener()

        return view
    }

    override fun onStart() {
        super.onStart()

        mActivities.clear()
        mItemIds.clear()
    }

    private fun setupActivityListener() {

        mLoader.apply {

            visibility = View.VISIBLE
        }
        mCreateNewActivityButton.apply {
            setOnClickListener {
                findNavController().navigate(R.id.action_showCreateActivityFragment)
            }
        }

        setupRecyclerView()
        mRecyclerView.addOnScrollListener(RealtimeScrollListener(::getActivities))

        getActivities()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }

    private fun setupRecyclerView() {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter =
            ActivitiesListAdapter(
                mActivities,
                ::navigateToDetails,
                ::createRecord
            )

        mRecyclerView.apply {

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
        mItemIds.removeAt(index)

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

            val action = ActivitiesListFragmentDirections
                .actionShowActivityDetails(mActivities[index], activityId)

            findNavController().navigate(action)
        }
    }

    private fun createRecord(index: Int, time: Int) {
        activitiesListViewModel?.createRecord(mActivities[index].title, time, mItemIds[index])
    }
}
