package com.jdevs.timeo.ui.activitydetail

import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.util.extensions.inflate
import com.jdevs.timeo.util.extensions.setAutoBounds
import kotlinx.android.synthetic.main.achievements_item.view.achievement_text

class AchievementsAdapter : RecyclerView.Adapter<AchievementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(parent.inflate(R.layout.achievements_item))

    override fun getItemCount() = 7

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setText(
            when (position % AWARDS_COUNT) {
                0 -> R.string._500_hours
                1 -> R.string.nice
                else -> R.string.cool
            }
        )

        holder.setDrawable(
            when (position % AWARDS_COUNT) {
                0 -> R.drawable.ic_award
                1 -> R.drawable.ic_flower
                else -> R.drawable.ic_snow
            }
        )
    }

    class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setText(@StringRes resId: Int) = view.achievement_text.setText(resId)
        fun setDrawable(@DrawableRes resId: Int) {

            val drawable = view.achievement_text.context.getDrawable(resId)
            drawable?.setAutoBounds()
            view.achievement_text.setCompoundDrawables(null, drawable, null, null)
        }
    }

    companion object {
        private const val AWARDS_COUNT = 3
    }
}
