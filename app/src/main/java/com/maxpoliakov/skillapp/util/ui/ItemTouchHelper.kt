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
import com.maxpoliakov.skillapp.ui.skills.SkillGroupFooterViewHolder
import com.maxpoliakov.skillapp.ui.skills.SkillListAdapter
import com.maxpoliakov.skillapp.ui.skills.SkillViewHolder
import com.maxpoliakov.skillapp.ui.skills.group.SkillGroupViewHolder
import kotlin.math.abs
import kotlin.math.min

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onLeaveGroup(skill: Skill)
    fun onDropped(change: Change?)
}

sealed class Change {
    abstract val skill: Skill

    class CreateGroup(override val skill: Skill, val otherSkill: Skill, val position: Int) : Change()
    class AddToGroup(override val skill: Skill, val groupId: Int) : Change()
}

private data class Coordinates(val top: Float, val bottom: Float)

fun createReorderAndGroupItemTouchHelper(
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

            if (viewHolder is SkillViewHolder) {
                val skill = viewHolder.viewModel.skill.value!!
                val movingUp = from > to

                val prevViewHolder = recyclerView.findViewHolderForAdapterPosition(if (movingUp) to - 1 else to)
                val nextViewHolder = recyclerView.findViewHolderForAdapterPosition(if (movingUp) to else to + 1)

                val insideGroup = isInsideGroup(prevViewHolder, nextViewHolder)
                viewHolder.isSmall = insideGroup

                if (skill.groupId != -1 && !insideGroup)
                    callback.onLeaveGroup(skill)
                else callback.onMove(from, to)
            } else callback.onMove(from, to)

            return true
        }

        private fun isInsideGroup(
            prevViewHolder: RecyclerView.ViewHolder?,
            nextViewHolder: RecyclerView.ViewHolder?
        ): Boolean {
            if (prevViewHolder == null || nextViewHolder == null) return false

            return (prevViewHolder is SkillViewHolder && prevViewHolder.viewModel.skill.value!!.groupId != -1
                    || prevViewHolder is SkillGroupViewHolder)
                    && (nextViewHolder is SkillViewHolder && nextViewHolder.viewModel.skill.value!!.groupId != -1
                    || nextViewHolder is SkillGroupFooterViewHolder)
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

            if (viewHolder !is SkillViewHolder) return

            for (i in 0 until recyclerView.childCount) {
                val holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i))

                if (holder == viewHolder || holder !is SkillViewHolder
                    || holder.viewModel.skill.value!!.groupId != -1) continue

                holder.isHighlighted = closeEnough(currentCoordinates!!, holder)
            }
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

            if (change == null) {
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

                if (secondSkill.groupId == -1 && closeEnough(dropCoordinates, closestViewHolder)) {
                    val position = min(viewHolder.absoluteAdapterPosition, closestViewHolder.absoluteAdapterPosition) - 1
                    return Change.CreateGroup(skill, secondSkill, position)
                }

                return null
            }

            return null
        }

        private fun closeEnough(
            dropCoordinates: Coordinates,
            closestViewHolder: RecyclerView.ViewHolder
        ): Boolean {
            return dropCoordinates.top > closestViewHolder.itemView.top - 55.dp.toPx(context)
                    && dropCoordinates.bottom < closestViewHolder.itemView.bottom + 55.dp.toPx(context)
        }

        private fun groupIfNecessary(skill: Skill, position: Int, adapter: SkillListAdapter): Change? {
            if (position == 0) return null

            val prevItem = adapter.getItem(position - 1)

            if (prevItem is Skill && prevItem.groupId != -1) {
                return Change.AddToGroup(skill, prevItem.groupId)
            } else if (prevItem is SkillGroup) {
                return Change.AddToGroup(skill, prevItem.id)
            }

            return null
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}

fun createReorderItemTouchHelper(callback: ItemTouchHelperCallback): ItemTouchHelper {
    val simpleItemTouchCallback = object : SimpleCallback(UP or DOWN, 0) {
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

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
            super.clearView(recyclerView, viewHolder)
            callback.onDropped(null)
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}
