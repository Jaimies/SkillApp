package com.jdevs.timeo

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

open class FragmentWithRecordsListRecyclerView : FragmentWithActionBarNavigation() {

    fun addItemsToRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        val viewAdapter = RecordsListAdapter(arrayOf("Activity 1", "Activity 2"))


        recyclerView.apply {

            layoutManager = viewManager

            adapter = viewAdapter

        }

    }

}