package com.jdevs.timeo.ui.stats

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.jdevs.timeo.util.StatsConstants.STATS_TYPES_COUNT

class StatsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = STATS_TYPES_COUNT
    override fun createFragment(position: Int) = StatsItemFragment(position)
}
