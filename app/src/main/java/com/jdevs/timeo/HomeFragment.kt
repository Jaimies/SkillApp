package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.model.FragmentWithActivitiesListRecyclerView

class HomeFragment : FragmentWithActivitiesListRecyclerView() {

    private lateinit var auth: FirebaseAuth

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
