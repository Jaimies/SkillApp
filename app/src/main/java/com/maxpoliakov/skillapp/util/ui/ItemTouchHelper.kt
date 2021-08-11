package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.skills.SkillViewHolder
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import kotlin.math.abs

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onGroup(first: Skill, second: Skill)
    fun onGroup(skill: Skill, skillGroup: SkillGroup)
    fun onDropped()
}

fun createDraggingItemTouchHelper(
    context: Context,
    callback: ItemTouchHelperCallback
): ItemTouchHelper {

    val simpleItemTouchCallback = object : SimpleCallback(UP or DOWN, 0) {
        private var draggedItemY = -1f
        private var lastDropY = -1f

        override fun isLongPressDragEnabled() = false

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {

            val from = viewHolder.absoluteAdapterPosition
            val to = target.absoluteAdapterPosition
            callback.onMove(from, to)

            return true
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            draggedItemY = viewHolder.itemView.y
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                lastDropY = draggedItemY
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            var closestViewHolder: RecyclerView.ViewHolder? = null
            var distanceToViewHolder = Float.POSITIVE_INFINITY

            for (i in 0 until recyclerView.childCount) {
                val holder = recyclerView.findViewHolderForAdapterPosition(i)
                val distance = abs(holder!!.itemView.y - lastDropY)
                if (distance < distanceToViewHolder) {
                    distanceToViewHolder = distance
                    closestViewHolder = holder
                }
            }

            fireGroupingCallbacks(distanceToViewHolder, viewHolder, closestViewHolder)
            callback.onDropped()
        }

        private fun fireGroupingCallbacks(
            distanceToViewHolder: Float,
            viewHolder: RecyclerView.ViewHolder,
            closestViewHolder: RecyclerView.ViewHolder?,
        ) {
            if (viewHolder !is SkillViewHolder || distanceToViewHolder > 20.dp.toPx(context)) return

            val skill = viewHolder.viewModel.skill.value!!

            if (closestViewHolder is SkillViewHolder) {
                val secondSkill = closestViewHolder.viewModel.skill.value!!
                callback.onGroup(skill, secondSkill)
            }

            if (closestViewHolder is SkillGroupViewHolder) {
                val group = closestViewHolder.viewModel.skillGroup.value!!
                callback.onGroup(skill, group)
            }
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}
