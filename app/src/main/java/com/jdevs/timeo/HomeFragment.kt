package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.navigation.fragment.findNavController

class HomeFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        records = Data(context)
        records.addActivitiesToElement(R.id.activities, view, findNavController())

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
