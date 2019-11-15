package com.jdevs.timeo.adapters

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.RecordActivityDialog
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.utilities.TimeUtility
import kotlinx.android.synthetic.main.partial_activities_list_item.view.listItemTitle
import kotlinx.android.synthetic.main.partial_activities_list_item.view.plusButton
import kotlinx.android.synthetic.main.partial_activities_list_item.view.totalHoursTextView

class ActivitiesListAdapter(
    private val dataset: ArrayList<TimeoActivity>,
    private val createRecord: (Int, Int) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    inner class ViewHolder(val layout: ConstraintLayout) : RecyclerView.ViewHolder(layout),
        View.OnClickListener {

        init {
            layout.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            navigateToDetails(adapterPosition)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.partial_activities_list_item,
                parent,
                false
            ) as ConstraintLayout

        return ViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            recolorView(
                position,
                rootView.background.current as LayerDrawable,
                plusButton.background.current as LayerDrawable,
                context
            )

            listItemTitle.apply {

                text = dataset[position].title
            }

            plusButton.apply {

                setOnClickListener {

                    val dialog =
                        RecordActivityDialog(
                            context,
                            position,
                            createRecord
                        )

                    dialog.show()
                }
            }

            val hours = TimeUtility.minsToHours(dataset[position].totalTime)
            totalHoursTextView.text = "${hours}h"
        }
    }

    override fun getItemCount() = dataset.size

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

        val borderLeft = parentDrawable.findDrawableByLayerId(R.id.borderLeft) as GradientDrawable
        val plusBackgroundItem =
            plusButtonDrawable.findDrawableByLayerId(R.id.plusBackground) as GradientDrawable

        borderLeft.setColor(color)
        plusBackgroundItem.setColor(color)
    }
}
