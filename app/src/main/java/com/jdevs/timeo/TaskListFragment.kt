package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class TaskListFragment : ActivitiesListFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)


        mRecyclerView = view.activitiesRecyclerView

        mCreateNewActivityView = view.createNewActivityView

        mCreateNewActivityButton = view.createNewActivityButton

        return view

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }

}