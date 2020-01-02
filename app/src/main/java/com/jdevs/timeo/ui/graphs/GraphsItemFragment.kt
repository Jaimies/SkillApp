package com.jdevs.timeo.ui.graphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.R
import com.jdevs.timeo.util.GraphTypes.DAY
import com.jdevs.timeo.util.GraphTypes.WEEK
import kotlinx.android.synthetic.main.graphs_item_frag.view.recycler_view

class GraphsItemFragment(private val type: Int) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.graphs_item_frag, container, false).apply {

            val text = getString(

                when (type) {

                    DAY -> R.string.day_stats
                    WEEK -> R.string.week_stats
                    else -> R.string.month_stats
                }
            )

            recycler_view.apply {

                layoutManager = LinearLayoutManager(context)
                adapter = GraphsRecyclerViewAdapter(text)
            }
        }
    }
}
