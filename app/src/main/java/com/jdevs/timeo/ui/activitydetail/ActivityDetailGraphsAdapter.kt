package com.jdevs.timeo.ui.activitydetail

import android.view.View
import androidx.fragment.app.Fragment
import com.jdevs.timeo.ui.graphs.GraphsItemFragment
import com.jdevs.timeo.ui.graphs.GraphsPagerAdapter

class ActivityDetailGraphsAdapter(
    fragment: Fragment,
    private val onClick: (View) -> Unit = {}
) : GraphsPagerAdapter(fragment) {

    override fun createFragment(position: Int) = GraphsItemFragment(onClick)
}