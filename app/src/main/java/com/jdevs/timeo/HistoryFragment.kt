package com.jdevs.timeo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.model.ActionBarFragment
import com.jdevs.timeo.model.RecordsListAdapter


class HistoryFragment : ActionBarFragment() {

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


    private fun addItemsToRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        val viewAdapter = RecordsListAdapter(
            arrayOf(
                Pair("TimeoActivity 1", 25),
                Pair("TimeoActivity 2", 25),
                Pair("TimeoActivity 1", 25),
                Pair("TimeoActivity 2", 25),
                Pair("TimeoActivity 1", 25),
                Pair("TimeoActivity 2", 25)
            )
        )


        recyclerView.apply {

            layoutManager = viewManager

            adapter = viewAdapter

        }

    }


}