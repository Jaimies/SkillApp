package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class TaskListFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout

    private lateinit var mActivitiesRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_task_list, container, false)

        view.apply {

            mLoader = listLoader as FrameLayout

            mActivitiesRecyclerView = activitiesRecyclerView

            mCreateNewActivityView = createNewActivityView
            mCreateNewActivityButton = createNewActivityButton
        }

        return view
    }

    override fun onStart() {

        super.onStart()

        setupActivityListener(
            mActivitiesRecyclerView,
            mLoader,
            mCreateNewActivityView,
            mCreateNewActivityButton
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
