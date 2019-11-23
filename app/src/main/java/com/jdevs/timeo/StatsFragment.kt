package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.models.ActionBarFragment

class StatsFragment : ActionBarFragment() {

    override val menuId = R.menu.action_bar_stats

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }
}
