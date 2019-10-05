package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController

class TaskListFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        records = Data(context)
        records.addActivitiesToView(R.id.activities, view, findNavController())

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}