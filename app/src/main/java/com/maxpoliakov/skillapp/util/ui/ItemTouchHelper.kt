package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.ui.skills.SkillViewHolder
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import kotlin.math.abs
import kotlin.math.min

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onGroup(first: Skill, second: Skill)
    fun onGroup(skillId: Int, skillGroupId: Int)
    fun onDropped()
}

private data class Coordinates(val top: Float, val bottom: Float)

fun createDraggingItemTouchHelper(
    context: Context,
    callback: ItemTouchHelperCallback
): ItemTouchHelper {

    val simpleItemTouchCallback = object : SimpleCallback(UP or DOWN, 0) {
        private var currentCoordinates: Coordinates? = null
        private var dropCoordinates: Coordinates? = null

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
            currentCoordinates = viewHolder.itemView.run { Coordinates(y, y + height) }
        }

        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                dropCoordinates = currentCoordinates
            }
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            val dropCoordinates = dropCoordinates ?: return

            var closestViewHolder: RecyclerView.ViewHolder? = null
            var distanceToViewHolder = Float.POSITIVE_INFINITY

            for (i in 0 until recyclerView.childCount) {
                val child = recyclerView.getChildAt(i)
                val params = child.layoutParams as RecyclerView.LayoutParams
                val position = params.absoluteAdapterPosition
                val holder = recyclerView.findViewHolderForAdapterPosition(position)

                if (holder == null || holder == viewHolder) continue
                val topDistance = abs(holder.itemView.top - dropCoordinates.top)
                val bottomDistance = abs(holder.itemView.bottom - dropCoordinates.bottom)

                val distance = min(topDistance, bottomDistance)

                if (distance < distanceToViewHolder) {
                    distanceToViewHolder = distance
                    closestViewHolder = holder
                }
            }

            fireGroupingCallbacks(dropCoordinates, viewHolder, closestViewHolder)
            callback.onDropped()
        }

        private fun fireGroupingCallbacks(
            dropCoordinates: Coordinates,
            viewHolder: RecyclerView.ViewHolder,
            closestViewHolder: RecyclerView.ViewHolder?,
        ) {
            if (closestViewHolder == null || viewHolder !is SkillViewHolder) return

            val skill = viewHolder.viewModel.skill.value!!

            if (closestViewHolder is SkillViewHolder) {
                val secondSkill = closestViewHolder.viewModel.skill.value!!

                if (secondSkill.groupId != -1)
                    callback.onGroup(skill.id, secondSkill.groupId)
                else if (nearEnough(dropCoordinates, closestViewHolder))
                    callback.onGroup(skill, secondSkill)
            }

            if (closestViewHolder is SkillGroupViewHolder) {
                val group = closestViewHolder.viewModel.skillGroup.value!!
                callback.onGroup(skill.id, group.id)
            }
        }

        private fun nearEnough(
            dropCoordinates: Coordinates,
            closestViewHolder: RecyclerView.ViewHolder
        ): Boolean {
            return dropCoordinates.top > closestViewHolder.itemView.top - 20.dp.toPx(context)
                    && dropCoordinates.bottom < closestViewHolder.itemView.bottom + 20.dp.toPx(context)
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}
