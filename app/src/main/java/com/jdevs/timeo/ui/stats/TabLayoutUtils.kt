package com.jdevs.timeo.ui.stats

import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.jdevs.timeo.R

fun setupTabLayoutMediator(tabLayout: TabLayout, viewPager: ViewPager2) {

    TabLayoutMediator(tabLayout, viewPager) { tab, position ->

        viewPager.setCurrentItem(tab.position, true)

        tab.text = tabLayout.context.getString(

            when (position) {
                0 -> R.string.day
                1 -> R.string.week
                else -> R.string.month
            }
        )
    }.attach()
}
