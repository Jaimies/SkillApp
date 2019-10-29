package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import com.jdevs.timeo.model.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class HomeFragment : ActivitiesListFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)


        initializeRecyclerView(view.activitiesRecyclerView)


        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
