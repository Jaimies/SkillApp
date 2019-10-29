package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.FragmentWithActivitiesListRecyclerView

class TaskListFragment : FragmentWithActivitiesListRecyclerView() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        val activitiesRecyclerView = view.findViewById<RecyclerView>(R.id.activitiesRecyclerView)

        addItemsToRecyclerView(activitiesRecyclerView)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}