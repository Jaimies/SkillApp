package com.jdevs.timeo.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class StatsViewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = STATS_TYPES_COUNT
    override fun createFragment(type: Int): StatsItemFragment {

        val bundle = Bundle().also { it.putInt(StatsItemFragment.TYPE, type) }
        return StatsItemFragment().also { it.arguments = bundle }
    }
}
