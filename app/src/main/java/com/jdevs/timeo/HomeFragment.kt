package com.jdevs.timeo

import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController

class HomeFragment : FragmentWithActionBarNavigation() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.goToRecordActivityBtn)

        button.setOnClickListener {
            it.findNavController().navigate(R.id.viewRecordActivityFragment)
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
