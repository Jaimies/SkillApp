package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.navigation.findNavController

class HomeFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        records = Data(context)
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        records.addActivitiesToElement(R.id.activities, view)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
