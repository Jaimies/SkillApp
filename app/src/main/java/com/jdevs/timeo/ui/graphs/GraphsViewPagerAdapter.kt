package com.jdevs.timeo.ui.graphs

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jdevs.timeo.util.StatsConstants.GRAPH_TYPES_COUNT

class GraphsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = GRAPH_TYPES_COUNT

    override fun createFragment(position: Int) = GraphsItemFragment(position)
}
