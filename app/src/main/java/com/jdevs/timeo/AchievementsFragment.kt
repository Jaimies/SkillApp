package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.models.ActionBarFragment

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
