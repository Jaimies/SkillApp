package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.Toast

class TaskListFragment : FragmentWithActionBarNavigation() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_list_tasks, container, false)

//        Data().addActivitiesToElement(context, R.id.layout_activities, view)
        Data().createElement(context, "Programming", view.findViewById(R.id.main_list_tasks))

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}