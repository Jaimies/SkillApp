package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class HomeFragment : ActivitiesListFragment() {

    private lateinit var mBottomNavView : BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mRecyclerView = view.activitiesRecyclerView

        mCreateNewActivityView = view.createNewActivityView

        mCreateNewActivityButton = view.createNewActivityButton


        activity?.apply {

            mBottomNavView = findViewById(R.id.bottomNavigationView)

            if(mBottomNavView.visibility != View.VISIBLE) {

                mBottomNavView.visibility = View.VISIBLE

            }

        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)

    }

}
