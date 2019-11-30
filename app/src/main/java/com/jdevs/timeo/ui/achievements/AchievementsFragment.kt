package com.jdevs.timeo.ui.achievements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment

class AchievementsFragment : ActionBarFragment() {

    override val menuId = R.menu.action_bar_achievements

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_achievements, container, false)
    }
}
