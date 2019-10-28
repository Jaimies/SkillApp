package com.jdevs.timeo

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.partial_activities_list_item.view.*

class ActivitiesListAdapter(private val dataset: Array<String>) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_activities_list_item, parent, false) as LinearLayout

        return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            run {
                val colorId = when (position.rem(6)) {

                    0 -> android.R.color.holo_red_dark
                    1 -> android.R.color.holo_green_dark
                    2 -> android.R.color.holo_blue_dark
                    else -> android.R.color.holo_orange_dark

                }

                val color = ContextCompat.getColor(context, colorId)


                val parentBackground = background.current as LayerDrawable
                val plusBackground = plusButton.background.current as LayerDrawable

                val borderLeft = parentBackground.findDrawableByLayerId(R.id.borderLeft) as GradientDrawable
                val plusBackgroundItem = plusBackground.findDrawableByLayerId(R.id.plusBackground) as GradientDrawable

                borderLeft.setColor(color)
                plusBackgroundItem.setColor(color)
            }


            listItemTitle.apply {

                text = dataset[position]

            }
        }

    }

    override fun getItemCount() = dataset.size
}