package com.jdevs.timeo.adapters

import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.RecordActivityDialog
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.ActivitiesItemBinding
import com.jdevs.timeo.navigators.ActivityNavigator
import com.jdevs.timeo.util.randomString
import com.jdevs.timeo.viewmodels.ActivityViewModel


class ActivitiesListAdapter(
    private val activityList: List<TimeoActivity>,
    private val createRecord: (Int, Int) -> Unit = { _, _ -> },
    private val navigateToDetails: (Int) -> Unit = {}
) : RecyclerView.Adapter<ActivitiesListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ActivitiesItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )

        binding.viewmodel = ViewModelProviders.of(parent.context as FragmentActivity).get(
            randomString(),
            ActivityViewModel::class.java
        )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindActivity(activityList[position])
    }

    override fun getItemCount() = activityList.size

    inner class ViewHolder(private val binding: ActivitiesItemBinding) :
        RecyclerView.ViewHolder(binding.root),
        ActivityNavigator {

        init {
            binding.viewmodel?.navigator = this
        }

        fun bindActivity(activity: TimeoActivity) {
            binding.viewmodel?.setActivity(activity)
            recolorView()
        }

        override fun showRecordDialog() {
            RecordActivityDialog(binding.root.context, adapterPosition, createRecord).show()
        }

        override fun navigateToDetails() {
            navigateToDetails(adapterPosition)
        }

        private fun recolorView() {

            val colorId = when (adapterPosition.rem(4)) {
                0 -> android.R.color.holo_red_dark
                1 -> android.R.color.holo_green_dark
                2 -> android.R.color.holo_blue_dark
                else -> android.R.color.holo_orange_dark
            }

            val color = ContextCompat.getColor(binding.root.context, colorId)

            val plusButtonDrawable = binding.plusButton.background as LayerDrawable
            val parentDrawable = binding.root.background as LayerDrawable

            val borderLeft = parentDrawable
                .findDrawableByLayerId(R.id.borderLeft) as GradientDrawable

            val plusBackgroundItem =
                plusButtonDrawable.findDrawableByLayerId(R.id.plusBackground) as GradientDrawable

            borderLeft.setColor(color)
            plusBackgroundItem.setColor(color)
        }
    }
}
