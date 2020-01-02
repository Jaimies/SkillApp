package com.jdevs.timeo.ui.graphs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.R
import kotlinx.android.synthetic.main.graphs_item_frag.view.recycler_view

class GraphsItemFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.graphs_item_frag, container, false).also {

            it.recycler_view.apply {

                layoutManager = LinearLayoutManager(context)
                adapter = GraphsRecyclerViewAdapter()
            }
        }
    }
}
