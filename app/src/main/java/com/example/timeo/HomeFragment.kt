package com.example.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController

class HomeFragment : Fragment() {
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)
        val button = view.findViewById<Button>(R.id.goToRecordActivityBtn)

//        navController = view.findNavController()

        button.setOnClickListener { view->
            view.findNavController().navigate(R.id.viewRecordActivityFragment)
        }

        // Inflate the layout for this fragment
        return view
    }
}
