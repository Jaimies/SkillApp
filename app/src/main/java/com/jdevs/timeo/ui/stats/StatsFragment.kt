package com.jdevs.timeo.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.jdevs.timeo.R
import kotlinx.android.synthetic.main.stats_frag.view.stats_tablayout
import kotlinx.android.synthetic.main.stats_frag.view.stats_viewpager

class StatsFragment : Fragment() {

    private val args: StatsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.stats_frag, container, false).also {

        it.stats_viewpager.adapter = StatsViewPagerAdapter(this)

        setupTabLayoutMediator(it.stats_tablayout, it.stats_viewpager)

        it.stats_viewpager.setCurrentItem(args.statsType, false)
    }
}
