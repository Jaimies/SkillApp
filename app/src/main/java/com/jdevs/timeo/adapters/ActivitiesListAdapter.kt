package com.jdevs.timeo.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.RecordActivityDialog
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.util.Time
import kotlinx.android.synthetic.main.partial_activities_item.view.listItemTitle
import kotlinx.android.synthetic.main.partial_activities_item.view.plusButton
import kotlinx.android.synthetic.main.partial_activities_item.view.totalHoursTextView

class ActivitiesListAdapter(
    private val activityList: List<TimeoActivity>,
    private val createRecord: (Int, Int) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.partial_activities_item,
                parent,
                false
            ) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindActivity(activityList[position])
    }

    override fun getItemCount() = activityList.size

    inner class ViewHolder(view: ConstraintLayout) : RecyclerView.ViewHolder(view) {
        private val listItemTitle = view.listItemTitle
        private val plusButton = view.plusButton
        private val rootView = view.rootView
        private val totalHoursTextView = view.totalHoursTextView

        init {
            view.setOnClickListener {
                navigateToDetails(adapterPosition)
            }
        }

        fun bindActivity(activity: TimeoActivity) {

            recolorView(
                adapterPosition,
                rootView.background.current as LayerDrawable,
                plusButton.background.current as LayerDrawable,
                rootView.context
            )

            listItemTitle.apply {

                text = activity.name
            }

            plusButton.apply {

                setOnClickListener {

                    val dialog =
                        RecordActivityDialog(
                            context,
                            adapterPosition,
                            createRecord
                        )

                    dialog.show()
                }
            }

            val hours = Time.minsToHours(activity.totalTime)
            totalHoursTextView.text = "${hours}h"
        }

        private fun recolorView(
            position: Int,
            parentDrawable: LayerDrawable,
            plusButtonDrawable: LayerDrawable,
            context: Context
        ) {

            val colorId = when (position.rem(4)) {

                0 -> android.R.color.holo_red_dark
                1 -> android.R.color.holo_green_dark
                2 -> android.R.color.holo_blue_dark
                else -> android.R.color.holo_orange_dark
            }

            val color = ContextCompat.getColor(context, colorId)

            val borderLeft =
                parentDrawable.findDrawableByLayerId(R.id.borderLeft) as GradientDrawable
            val plusBackgroundItem =
                plusButtonDrawable.findDrawableByLayerId(R.id.plusBackground) as GradientDrawable

            borderLeft.setColor(color)
            plusBackgroundItem.setColor(color)
        }
    }
}
