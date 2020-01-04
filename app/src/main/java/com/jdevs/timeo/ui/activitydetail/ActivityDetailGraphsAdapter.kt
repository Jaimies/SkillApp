package com.jdevs.timeo.ui.activitydetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jdevs.timeo.util.StatsConstants.GRAPH_TYPES_COUNT

class ActivityDetailGraphsAdapter(
    fragment: Fragment,
    private val onClick: (Int) -> Unit = {}
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = GRAPH_TYPES_COUNT

    override fun createFragment(position: Int) = ActivityDetailGraphsFragment(onClick, position)
}
