package com.jdevs.timeo.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment

class StatsFragment : ActionBarFragment() {

    override val menuId = R.menu.action_bar_stats

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_stats, container, false)
    }
}
