package com.jdevs.timeo.ui.activitydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R

class ActivityDetailGraphsFragment(private val onClick: (View) -> Unit) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activitydetail_graphs_frag, container, false).also {

            it.setOnClickListener(onClick)
        }
    }
}
