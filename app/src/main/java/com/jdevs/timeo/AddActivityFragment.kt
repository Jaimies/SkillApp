package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class AddActivityFragment : DataManipulatorFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Records.addActivity(context, "Programming", "Laptop.png")
        val activities = Records.listActivities(context)

//        Iterate over activities
        for (activity in activities) {
            Toast.makeText(context, activity.name, Toast.LENGTH_LONG).show()
            Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_activity, container, false)
    }
}
