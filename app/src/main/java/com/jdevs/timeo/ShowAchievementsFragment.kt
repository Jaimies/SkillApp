package com.jdevs.timeo


import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment


class ShowAchievementsFragment : FragmentWithActionBarNavigation() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_achievements, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        addOptionsMenu(menu, inflater, R.menu.action_bar_achievements)
    }
}
