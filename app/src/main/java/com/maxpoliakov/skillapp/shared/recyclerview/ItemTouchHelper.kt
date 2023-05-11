package com.maxpoliakov.skillapp.shared.recyclerview

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.maxpoliakov.skillapp.domain.model.MeasurementUnit
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.domain.model.Trackable
import com.maxpoliakov.skillapp.shared.Dimension
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.ui.skills.recyclerview.SkillListAdapter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.maxpoliakov.skillapp.ui.skills.recyclerview.skill.SkillViewHolder
import kotlin.math.abs
import kotlin.math.min

interface ItemTouchHelperCallback {
    fun onMove(from: Int, to: Int)
    fun onLeaveGroup(skill: Skill)
    fun onDropped(change: Change?)
}

sealed class Change {
    abstract val skill: Skill

    class CreateGroup(override val skill: Skill, val otherSkill: Skill) : Change()
    class AddToGroup(override val skill: Skill, val groupId: Int) : Change()
}

private data class Coordinates(val top: Float, val bottom: Float)

class SimpleCallbackImpl(
    private val callback: ItemTouchHelperCallback,
    private val listAdapter: SkillListAdapter,
) : SimpleCallback(UP or DOWN, 0) {
    private var currentCoordinates: Coordinates? = null
    private var dropCoordinates: Coordinates? = null

    override fun isLongPressDragEnabled() = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        target: ViewHolder
    ): Boolean {

        if (viewHolder !is SkillViewHolder) {
            return false
        }

        val skill = listAdapter.getItem(viewHolder.absoluteAdapterPosition) as? Skill ?: return false
        val itemBelow = getItemBelow(viewHolder, target)

        val insideGroup = isInsideGroup(itemBelow)
        val areOfTheSameUnit = skill.unit == itemBelow?.unit

        viewHolder.isSmall = insideGroup && areOfTheSameUnit

        if (skill.isInAGroup && !insideGroup) {
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

    override fun canDropOver(recyclerView: RecyclerView, current: ViewHolder, target: ViewHolder): Boolean {
        return target.itemViewType != SkillListAdapter.ItemType.Stopwatch
    }

    private fun getItemBelow(viewHolder: ViewHolder, target: ViewHolder): Any? {
        val index = getPositionOfItemBelow(viewHolder, target)
        return listAdapter.getItemOrNull(index)
    }

    private fun getPositionOfItemBelow(viewHolder: ViewHolder, target: ViewHolder): Int {
        return if (isMovingUpRelativeToTarget(viewHolder, target)) target.absoluteAdapterPosition
        else target.absoluteAdapterPosition + 1
    }

    fun isMovingUpRelativeToTarget(viewHolder: ViewHolder, target: ViewHolder): Boolean {
        return viewHolder.absoluteAdapterPosition > target.absoluteAdapterPosition
    }

    fun isInsideGroup(itemBelow: Any?): Boolean {
        return itemBelow is Skill && itemBelow.isInAGroup
                || itemBelow is SkillGroupFooter
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

        callback.onDropped(
            getChange(recyclerView, viewHolder, dropCoordinates ?: return),
        )
    }

    private fun getChange(recyclerView: RecyclerView, viewHolder: ViewHolder, dropCoordinates: Coordinates): Change? {
        val position = viewHolder.absoluteAdapterPosition
        val skill = listAdapter.getItem(position) as? Skill ?: return null

        val groupId = getIdOfGroupToBeAddedTo(skill, position)
        if (groupId != -1 && groupId != skill.groupId) return Change.AddToGroup(skill, groupId)

        val secondSkill = getSkillToGroupWith(recyclerView, viewHolder, dropCoordinates) ?: return null
        if (skill.canBeInGroupWith(secondSkill)) return Change.CreateGroup(skill, secondSkill)

        return null
    }

    private fun getSkillToGroupWith(recyclerView: RecyclerView, viewHolder: ViewHolder, dropCoordinates: Coordinates): Skill? {
        return getClosestViewHolder(recyclerView, viewHolder, dropCoordinates)?.let { viewHolder ->
            listAdapter.getItem(viewHolder.absoluteAdapterPosition) as? Skill
        }?.takeIf { it.isNotInAGroup }
    }

    private fun getClosestViewHolder(
        recyclerView: RecyclerView,
        viewHolder: ViewHolder,
        dropCoordinates: Coordinates,
        minimumAcceptableDistance: Dimension = 55.dp,
    ): ViewHolder? {
        var closestViewHolder: ViewHolder? = null
        var distanceToViewHolder = Float.POSITIVE_INFINITY

        val minimumAcceptableDistanceInPx = minimumAcceptableDistance.toPx(recyclerView.context)

        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val holder = recyclerView.findContainingViewHolder(child)

            if (holder == null || holder == viewHolder) continue
            val distance = child.getDistanceFrom(dropCoordinates)

            if (distance < distanceToViewHolder && distance < minimumAcceptableDistanceInPx) {
                distanceToViewHolder = distance
                closestViewHolder = holder
            }
        }

        return closestViewHolder
    }

    private fun View.getDistanceFrom(coordinates: Coordinates): Float {
        val topDistance = abs(top - coordinates.top)
        val bottomDistance = abs(bottom - coordinates.bottom)

        return min(topDistance, bottomDistance)
    }

    private fun closeEnough(
        dropCoordinates: Coordinates,
        closestViewHolder: ViewHolder
    ): Boolean {
        val context = closestViewHolder.itemView.context

        return dropCoordinates.top > closestViewHolder.itemView.top - 55.dp.toPx(context)
                && dropCoordinates.bottom < closestViewHolder.itemView.bottom + 55.dp.toPx(context)
    }

    private fun getIdOfGroupToBeAddedTo(skill: Skill, position: Int): Int {
        val prevItem = listAdapter.getItemOrNull(position - 1)

        if (prevItem is Skill && prevItem.isInAGroup && skill.unit == prevItem.unit) {
            return prevItem.groupId
        } else if (prevItem is SkillGroup && skill.unit == prevItem.unit) {
            return prevItem.id
        }

        return -1
    }

    private val Any.unit: MeasurementUnit<*>? get() = when(this) {
        is Trackable -> this.unit
        is SkillGroupFooter -> this.group.unit
        else -> null
    }
}
