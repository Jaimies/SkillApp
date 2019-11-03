package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class TaskListFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)


        mCreateNewActivityView = view.createNewActivityView

        mCreateNewActivityButton = view.createNewActivityButton

        mLoader = view.listLoader as FrameLayout

        mRecyclerView = view.activitiesRecyclerView

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

}