package com.jdevs.timeo

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import kotlin.math.roundToInt

class HomeFragment : FragmentWithActionBarNavigation() {
    private lateinit var records : Data
    private lateinit var dateHandler: DateHandler

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_home, container, false)

        records = Data(context)
        records.addActivitiesToView(R.id.activities, view, findNavController())

        dateHandler = DateHandler(R.id.topbar, R.id.topbar_dayInMonth, R.id.topbar_weekday, R.id.topbar_monthAndYear, view)

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }
}
