package com.maxpoliakov.skillapp.ui.common

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.ui.skills.ITEM_TYPE_SKILL_GROUP_FOOTER
import com.maxpoliakov.skillapp.ui.skills.SkillGroupFooterViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillViewHolder
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import com.maxpoliakov.skillapp.util.ui.dp

class CardViewDecoration : ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val size16dp = 16.dp.toPx(parent.context).toFloat()
        val strokeColor = ContextCompat.getColor(parent.context, R.color.cardview_dark_background)

        val drawnGroupIds = mutableListOf<Int>()

        for (i in 0 until parent.childCount) {
            val viewHolder = parent.getChildViewHolder(parent.getChildAt(i))

            val groupId = if (viewHolder is SkillGroupViewHolder)
                viewHolder.viewModel.skillGroup.value!!.id
            else if (viewHolder is SkillViewHolder && viewHolder.viewModel.skill.value!!.groupId != -1)
                viewHolder.viewModel.skill.value!!.groupId
            else continue

            if (drawnGroupIds.contains(groupId)) continue

            drawnGroupIds.add(groupId)

            val lastViewHolder = parent.findLastViewHolderInGroup(groupId) ?: continue

            val paint = Paint().apply {
                color = strokeColor
                style = Paint.Style.STROKE
            }
            c.drawRoundRect(
                size16dp,
                viewHolder.itemView.top.toFloat(),
                viewHolder.itemView.right.toFloat(),
                lastViewHolder.itemView.bottom.toFloat(),
                size16dp,
                size16dp,
                paint
            )
        }
    }

    private fun RecyclerView.findLastViewHolderInGroup(groupId: Int): RecyclerView.ViewHolder? {
        for (i in childCount - 1 downTo 0) {
            val holder = getChildViewHolder(getChildAt(i))

            if (holder is SkillGroupViewHolder && holder.viewModel.skillGroup.value!!.id == groupId
                || holder is SkillViewHolder && holder.viewModel.skill.value!!.groupId == groupId
                || holder is SkillGroupFooterViewHolder && holder.groupId == groupId
            ) return holder
        }

        return null
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val padding16dp = 16.dp.toPx(parent.context)
        val params = view.layoutParams as RecyclerView.LayoutParams
        val position = params.absoluteAdapterPosition
        val viewType = parent.adapter!!.getItemViewType(position)

        if (viewType == ITEM_TYPE_SKILL_GROUP_FOOTER)
            outRect.set(0, 0, 0, 24.dp.toPx(parent.context))

        outRect.left = padding16dp
        outRect.right = padding16dp
    }
}
