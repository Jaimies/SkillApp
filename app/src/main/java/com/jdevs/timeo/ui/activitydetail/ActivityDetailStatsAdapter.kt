package com.jdevs.timeo.ui.activitydetail

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jdevs.timeo.util.StatsConstants.STATS_TYPES_COUNT

class ActivityDetailStatsAdapter(
    fragment: Fragment,
    private val onClick: (Int) -> Unit = {}
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = STATS_TYPES_COUNT
    override fun createFragment(position: Int) = ActivityDetailStatsFragment(onClick, position)
}
