package com.jdevs.timeo.ui.graphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.tabs.TabLayoutMediator
import com.jdevs.timeo.R
import kotlinx.android.synthetic.main.graphs_frag.view.graphs_tablayout
import kotlinx.android.synthetic.main.graphs_frag.view.graphs_viewpager

class GraphsFragment : Fragment() {

    private val args: GraphsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.graphs_frag, container, false).apply {

            graphs_viewpager.adapter = GraphsPagerAdapter(this@GraphsFragment)

            TabLayoutMediator(graphs_tablayout, graphs_viewpager) { tab, position ->

                graphs_viewpager.setCurrentItem(tab.position, true)

                tab.text = when (position) {
                    0 -> "Day"
                    1 -> "Week"
                    else -> "Month"
                }
            }.attach()

            graphs_viewpager.setCurrentItem(args.graphType, false)
        }
    }
}
