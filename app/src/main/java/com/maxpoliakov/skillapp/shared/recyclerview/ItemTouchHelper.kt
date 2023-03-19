package com.maxpoliakov.skillapp.shared.recyclerview

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListViewHolder
import com.maxpoliakov.skillapp.ui.skills.recyclerview.skill.SkillViewHolder
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.header.SkillGroupViewHolder
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

fun createReorderAndGroupItemTouchHelper(callback: ItemTouchHelperCallback): ItemTouchHelper {
    val simpleItemTouchCallback = object : SimpleCallback(UP or DOWN, 0) {
        private var currentCoordinates: Coordinates? = null
        private var dropCoordinates: Coordinates? = null

        override fun isLongPressDragEnabled() = false

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
            target: ViewHolder
        ): Boolean {

            if (viewHolder !is SkillViewHolder || target !is SkillListViewHolder) {
                return false
            }

            val skill = viewHolder.viewModel.skill.value!!

            val viewHolderBelow = getViewHolderBelow(recyclerView, viewHolder, target)
            val insideGroup = isInsideGroup(viewHolderBelow)
            val areOfTheSameUnit = viewHolder.unit == viewHolderBelow?.unit

            viewHolder.isSmall = insideGroup && areOfTheSameUnit

            if (skill.groupId != -1 && !insideGroup) {
                callback.onLeaveGroup(skill)
                return true
            }

            if (!insideGroup || areOfTheSameUnit) {
                val from = viewHolder.absoluteAdapterPosition
                val to = target.absoluteAdapterPosition

                callback.onMove(from, to)
                return true
            }

            return false
        }

        private fun getViewHolderBelow(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): SkillListViewHolder? {
            val index = getIndexOfViewHolderBelow(viewHolder, target)
            return recyclerView.findViewHolderForAdapterPosition(index) as? SkillListViewHolder
        }

        private fun getIndexOfViewHolderBelow(viewHolder: ViewHolder, target: ViewHolder): Int {
            return if (isMovingUpRelativeToTarget(viewHolder, target)) target.absoluteAdapterPosition
            else target.absoluteAdapterPosition + 1
        }

        fun isMovingUpRelativeToTarget(viewHolder: ViewHolder, target: ViewHolder): Boolean {
            return viewHolder.absoluteAdapterPosition > target.absoluteAdapterPosition
        }

        fun isInsideGroup(viewHolderBelow: SkillListViewHolder?): Boolean {
            return viewHolderBelow != null && viewHolderBelow.groupId != -1
                    && viewHolderBelow !is SkillGroupViewHolder
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: ViewHolder,
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

                if (holder == viewHolder || holder !is SkillViewHolder || holder.isInAGroup) continue

                holder.isHighlighted = closeEnough(currentCoordinates!!, holder) && holder.unit == viewHolder.unit
            }
        }

        override fun onSelectedChanged(viewHolder: ViewHolder?, actionState: Int) {
            super.onSelectedChanged(viewHolder, actionState)
            if (actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
                dropCoordinates = currentCoordinates
            }
        }

        override fun onSwiped(viewHolder: ViewHolder, direction: Int) {}

        override fun clearView(recyclerView: RecyclerView, viewHolder: ViewHolder) {
            super.clearView(recyclerView, viewHolder)

            viewHolder.itemView.translationZ = 0f
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
            viewHolder: ViewHolder,
            dropCoordinates: Coordinates,
        ): ViewHolder? {
            var closestViewHolder: ViewHolder? = null
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
            closestViewHolder: ViewHolder?,
        ): Change? {
            if (closestViewHolder == null) return null

            val skill = viewHolder.viewModel.skill.value!!

            if (closestViewHolder is SkillViewHolder) {
                val secondSkill = closestViewHolder.viewModel.skill.value!!

                if (secondSkill.groupId == -1 && closeEnough(dropCoordinates, closestViewHolder)
                    && skill.unit == secondSkill.unit
                ) {
                    val position = min(viewHolder.absoluteAdapterPosition, closestViewHolder.absoluteAdapterPosition) - 1
                    return Change.CreateGroup(skill, secondSkill, position)
                }

                return null
            }

            return null
        }

        private fun closeEnough(
            dropCoordinates: Coordinates,
            closestViewHolder: ViewHolder
        ): Boolean {
            val context = closestViewHolder.itemView.context

            return dropCoordinates.top > closestViewHolder.itemView.top - 55.dp.toPx(context)
                    && dropCoordinates.bottom < closestViewHolder.itemView.bottom + 55.dp.toPx(context)
        }

        private fun groupIfNecessary(skill: Skill, position: Int, adapter: SkillListAdapter): Change? {
            if (position <= 0) return null

            val prevItem = adapter.getItem(position - 1)

            if (prevItem is Skill && prevItem.groupId != -1 && skill.unit == prevItem.unit) {
                return Change.AddToGroup(skill, prevItem.groupId)
            } else if (prevItem is SkillGroup && skill.unit == prevItem.unit) {
                return Change.AddToGroup(skill, prevItem.id)
            }

            return null
        }
    }

    return ItemTouchHelper(simpleItemTouchCallback)
}