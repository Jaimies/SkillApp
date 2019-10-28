package com.jdevs.timeo

import android.os.Bundle
import android.view.*

class StatsFragment : FragmentWithActionBarNavigation() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_stats)
    }
}
