package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : FragmentWithActivitiesListRecyclerView() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val activitiesRecyclerView = view.findViewById<RecyclerView>(R.id.activitiesRecyclerView)

        addItemsToRecyclerView(activitiesRecyclerView)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
