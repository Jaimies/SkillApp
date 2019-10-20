package com.jdevs.timeo

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class FragmentWithActivitiesListRecyclerView : FragmentWithActionBarNavigation() {

    fun addItemsToRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        val viewAdapter = ActivitiesListAdapter(arrayOf("Activity 1", "Activity 2"))


        recyclerView.apply {

            layoutManager = viewManager

            adapter = viewAdapter

        }

    }

}