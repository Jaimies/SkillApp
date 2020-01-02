package com.jdevs.timeo.ui.activitydetail

import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ActivityDetailGraphsAdapter(
    fragment: Fragment,
    private val onClick: (View) -> Unit = {}
) : FragmentStateAdapter(fragment) {

    override fun getItemCount() = 3
    override fun createFragment(position: Int) = ActivityDetailGraphsFragment(onClick)
}