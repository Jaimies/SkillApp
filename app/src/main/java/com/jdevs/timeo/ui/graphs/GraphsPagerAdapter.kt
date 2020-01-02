package com.jdevs.timeo.ui.graphs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class GraphsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3
    override fun createFragment(position: Int): GraphsItemFragment {

        return GraphsItemFragment(position)
    }
}
