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
import com.maxpoliakov.skillapp.ui.skills.SkillListAdapter
import com.maxpoliakov.skillapp.ui.skills.SkillViewHolder
import kotlin.math.abs
import kotlin.math.min

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onDropped(change: Change?)
}

sealed class Change {
    class Group(val first: Skill, val second: Skill) : Change()
    class AddToGroup(val skill: Skill, val groupId: Int) : Change()
    class RemoveFromGroup(val skill: Skill) : Change()
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

            val position = viewHolder.absoluteAdapterPosition
            val adapter = recyclerView.adapter

            if (adapter !is SkillListAdapter || viewHolder !is SkillViewHolder) return

            var change = groupIfNecessary(viewHolder.viewModel.skill.value!!, position, adapter)

            if (change == null || change is Change.RemoveFromGroup) {
                val closestViewHolder = getClosestViewHolder(recyclerView, viewHolder, dropCoordinates)
                val groupingChange = fireGroupingCallbacks(dropCoordinates, viewHolder, closestViewHolder)
                if (groupingChange != null) change = groupingChange
            }

            callback.onDropped(change)
        }

        private fun getClosestViewHolder(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dropCoordinates: Coordinates,
        ): RecyclerView.ViewHolder? {
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

            return closestViewHolder
        }

        private fun fireGroupingCallbacks(
            dropCoordinates: Coordinates,
            viewHolder: SkillViewHolder,
            closestViewHolder: RecyclerView.ViewHolder?,
        ): Change? {
            if (closestViewHolder == null) return null

            val skill = viewHolder.viewModel.skill.value!!

            if (closestViewHolder is SkillViewHolder) {
                val secondSkill = closestViewHolder.viewModel.skill.value!!

                if (secondSkill.groupId == -1 && nearEnough(dropCoordinates, closestViewHolder))
                    return Change.Group(skill, secondSkill)

                return null
            }

            return null
        }

        private fun nearEnough(
            dropCoordinates: Coordinates,
            closestViewHolder: RecyclerView.ViewHolder
        ): Boolean {
            return dropCoordinates.top > closestViewHolder.itemView.top - 20.dp.toPx(context)
                    && dropCoordinates.bottom < closestViewHolder.itemView.bottom + 20.dp.toPx(context)
        }

        private fun groupIfNecessary(skill: Skill, position: Int, adapter: SkillListAdapter): Change? {
            if (position == 0) {
                if (skill.groupId != -1)
                    return Change.RemoveFromGroup(skill)

                return null
            }

            val prevItem = adapter.getItem(position - 1)

            if (prevItem is Skill && prevItem.groupId != -1) {
                return Change.AddToGroup(skill, prevItem.groupId)
            } else if (prevItem is SkillGroup) {
                return Change.AddToGroup(skill, prevItem.id)
            } else if (skill.groupId != -1) {
                return Change.RemoveFromGroup(skill)
            }

            return null
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}
