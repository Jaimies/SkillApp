package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.Toast

class TaskListFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        records = Data(context)
        val view = inflater.inflate(R.layout.fragment_list_tasks, container, false)
        records.addActivitiesToElement(R.id.activities, view)
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}