package com.jdevs.timeo.ui.activitydetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.StatsTypes.DAY
import com.jdevs.timeo.util.StatsTypes.WEEK
import kotlinx.android.synthetic.main.activitydetail_stats_frag.view.title_text_view

class ActivityDetailStatsFragment(
    private val onClick: (Int) -> Unit,
    private val statsType: Int
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.activitydetail_stats_frag, container, false).apply {

            title_text_view.text = getString(

                when (statsType) {

                    DAY -> R.string.day_stats
                    WEEK -> R.string.week_stats
                    else -> R.string.month_stats
                }
            )

            setOnClickListener { onClick(statsType) }
        }
    }
}
