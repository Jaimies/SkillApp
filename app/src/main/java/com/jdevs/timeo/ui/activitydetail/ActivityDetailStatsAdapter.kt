package com.jdevs.timeo.ui.activitydetail

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.model.StatsTypes
import com.jdevs.timeo.ui.stats.STATS_TYPES_COUNT
import com.jdevs.timeo.util.extensions.inflate
import kotlinx.android.synthetic.main.activitydetail_stats_item.view.title_text_view

class ActivityDetailStatsAdapter(private val onClick: (Int) -> Unit = {}) :
    RecyclerView.Adapter<ActivityDetailStatsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.activitydetail_stats_item), viewType, onClick)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemViewType(position: Int) = position

    override fun getItemCount() = STATS_TYPES_COUNT

    class ViewHolder(view: View, statsType: Int, onClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(view) {

        init {
            view.title_text_view.setText(
                when (statsType) {

                    StatsTypes.DAY -> R.string.day_stats
                    StatsTypes.WEEK -> R.string.week_stats
                    else -> R.string.month_stats
                }
            )

            view.setOnClickListener { onClick(statsType) }
        }
    }
}
