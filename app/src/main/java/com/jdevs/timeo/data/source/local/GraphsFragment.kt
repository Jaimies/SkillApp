package com.jdevs.timeo.data.source.local

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.jdevs.timeo.R
import com.jdevs.timeo.ui.graphs.GraphsPagerAdapter
import kotlinx.android.synthetic.main.graphs_frag.view.graphs_tablayout
import kotlinx.android.synthetic.main.graphs_frag.view.graphs_viewpager

class GraphsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.graphs_frag, container, false)

        view.graphs_viewpager.adapter = GraphsPagerAdapter(this)

        TabLayoutMediator(view.graphs_tablayout, view.graphs_viewpager) { tab, position ->

            view.graphs_viewpager.setCurrentItem(tab.position, true)

            tab.text = when (position) {
                0 -> "Day"
                1 -> "Week"
                else -> "Month"
            }
        }.attach()

        return view
    }
}
