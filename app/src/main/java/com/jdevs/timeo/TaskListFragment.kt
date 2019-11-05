package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class TaskListFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        mLoader = view.listLoader as FrameLayout

        return view
    }

    override fun onStart() {

        super.onStart()

        setupActivityListener(
            view!!.activitiesRecyclerView,
            mLoader,
            view!!.createNewActivityView,
            view!!.createNewActivityButton
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
