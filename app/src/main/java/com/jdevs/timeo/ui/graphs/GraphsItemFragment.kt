package com.jdevs.timeo.ui.graphs


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R

class GraphsItemFragment(private val onClick: (View) -> Unit = {}) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.graphs_item_frag, container, false).also {

            it.setOnClickListener(onClick)
        }
    }
}
