package com.jdevs.timeo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView


class HistoryFragment : FragmentWithRecordsListRecyclerView() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        val activitiesRecyclerView = view.findViewById<RecyclerView>(R.id.recordsRecyclerView)

        addItemsToRecyclerView(activitiesRecyclerView)

        // Inflate the layout for this fragment
        return view

    }


}