package com.jdevs.timeo.ui.activitydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.GraphTypes.DAY
import com.jdevs.timeo.util.GraphTypes.WEEK
import kotlinx.android.synthetic.main.activitydetail_graphs_frag.view.title_text_view

class ActivityDetailGraphsFragment(
    private val onClick: (Int) -> Unit,
    private val type: Int
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activitydetail_graphs_frag, container, false).apply {

            title_text_view.text = getString(

                when (type) {

                    DAY -> R.string.day_stats
                    WEEK -> R.string.week_stats
                    else -> R.string.month_stats
                }
            )

            setOnClickListener { onClick(type) }
        }
    }
}
