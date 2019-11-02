package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*
import kotlinx.android.synthetic.main.partial_circular_loader.view.*

class HomeFragment : ActivitiesListFragment() {

    private lateinit var mLoader : FrameLayout
    private lateinit var mRecyclerView : RecyclerView

    private lateinit var mBottomNavView : BottomNavigationView

    private lateinit var mCreateNewActivityView : LinearLayout
    private lateinit var mCreateNewActivityButton : MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mCreateNewActivityView = view.createNewActivityView

        mCreateNewActivityButton = view.createNewActivityButton

        mLoader = view.spinningProgressBar

        mRecyclerView = view.activitiesRecyclerView


        showBottonNavIfHidden()

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()

        setupActivityListener(
            mRecyclerView,
            mLoader,
            mCreateNewActivityView,
            mCreateNewActivityButton
        )

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)

    }

    private fun showBottonNavIfHidden() {

        activity?.apply {

            mBottomNavView = findViewById(R.id.bottomNavView)

            if(mBottomNavView.visibility != View.VISIBLE) {

                mBottomNavView.visibility = View.VISIBLE

            }

        }

    }

}
