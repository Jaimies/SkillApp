package com.jdevs.timeo.models

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.HomeFragmentDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.RecordActivityDialog
import com.jdevs.timeo.TaskListFragmentDirections
import com.jdevs.timeo.data.TimeoActivity
import kotlinx.android.synthetic.main.partial_activities_list_item.view.*

class ActivitiesListAdapter(
    private val dataset: Array<TimeoActivity>,
    private val navController: NavController,
    private val mItemIds : ArrayList<String>
) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    inner class ViewHolder(val layout: LinearLayout) : RecyclerView.ViewHolder(layout),
        View.OnClickListener{

        init {

            layout.setOnClickListener(this)

        }


        override fun onClick(v: View?) {

            try {

                navController.currentDestination?.apply {

                    val title = dataset[adapterPosition].title
                    val icon = dataset[adapterPosition].icon

                    val activityId = mItemIds[adapterPosition]

                    try {

                        val action = HomeFragmentDirections
                            .actionShowActivityDetails(title, icon,  activityId)

                        navController.navigate(action)

                    } catch (e : IllegalArgumentException) {

                        val action = TaskListFragmentDirections
                            .actionShowActivityDetails(title, icon, activityId)

                        navController.navigate(action)

                    }

                }

            } catch (e : Exception) {}

        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val layout = LayoutInflater.from(parent.context)
            .inflate(R.layout.partial_activities_list_item, parent, false) as LinearLayout

        return ViewHolder(layout)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.layout.apply {

            reColorView(position, rootView.background.current, plusButton.background.current, context)


            listItemTitle.apply {

                text = dataset[position].title

            }

            plusButton.apply {

                setOnClickListener {

                    val dialog = RecordActivityDialog(context, dataset[position].title, mItemIds[position])

                    dialog.show()

                }

            }

        }

    }

    override fun getItemCount() = dataset.size




    private fun reColorView(position: Int, parentDrawable : Drawable, plusButtonDrawable : Drawable, context : Context) {

        val colorId = when (position.rem(4)) {

            0 -> android.R.color.holo_red_dark
            1 -> android.R.color.holo_green_dark
            2 -> android.R.color.holo_blue_dark
            else -> android.R.color.holo_orange_dark

        }

        val color = ContextCompat.getColor(context, colorId)


        val parentBackground = parentDrawable as LayerDrawable
        val plusBackground = plusButtonDrawable as LayerDrawable

        val borderLeft = parentBackground.findDrawableByLayerId(R.id.borderLeft) as GradientDrawable
        val plusBackgroundItem = plusBackground.findDrawableByLayerId(R.id.plusBackground) as GradientDrawable

        borderLeft.setColor(color)
        plusBackgroundItem.setColor(color)

    }

}