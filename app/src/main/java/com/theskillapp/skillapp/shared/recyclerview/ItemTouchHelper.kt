package com.theskillapp.skillapp.shared.recyclerview

import android.graphics.Canvas
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.SkillGroup
import com.theskillapp.skillapp.domain.model.Trackable
import com.theskillapp.skillapp.shared.Dimension
import com.theskillapp.skillapp.shared.Dimension.Companion.dp
import com.theskillapp.skillapp.ui.skills.recyclerview.SkillListAdapter
import com.theskillapp.skillapp.ui.skills.recyclerview.group.footer.SkillGroupFooter
import com.theskillapp.skillapp.ui.skills.recyclerview.skill.SkillViewHolder
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

private data class Coordinates(val top: Float, val bottom: Float) {
    companion object {
        fun ofView(view: View): Coordinates {
            return Coordinates(view.y, view.y + view.height)
        }
    }
}

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

        val currentCoordinates = Coordinates.ofView(viewHolder.itemView)
        this.currentCoordinates = currentCoordinates

        val viewHolderToGroupWith = getViewHolderOfSkillToGroupWith(recyclerView, viewHolder, currentCoordinates)

        for (i in 0 until recyclerView.childCount) {
            val holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i)) as? SkillViewHolder ?: continue
            holder.isHighlighted = holder == viewHolderToGroupWith
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
        val skill = listAdapter.getItemOrNull(position) as? Skill ?: return null

        val groupId = getIdOfGroupToBeAddedTo(skill, position)
        if (groupId != -1 && groupId != skill.groupId) return Change.AddToGroup(skill, groupId)

        val secondSkill = getSkillToGroupWith(recyclerView, viewHolder, dropCoordinates)
        if (secondSkill != null) return Change.CreateGroup(skill, secondSkill)

        return null
    }

    private fun getSkillToGroupWith(recyclerView: RecyclerView, viewHolder: ViewHolder, dropCoordinates: Coordinates): Skill? {
        return getViewHolderOfSkillToGroupWith(recyclerView, viewHolder, dropCoordinates)?.let { closestViewHolder ->
            listAdapter.getItem(closestViewHolder.absoluteAdapterPosition) as? Skill
        }
    }

    private fun getViewHolderOfSkillToGroupWith(recyclerView: RecyclerView, viewHolder: ViewHolder, dropCoordinates: Coordinates): ViewHolder? {
        return getClosestViewHolder(recyclerView, viewHolder, dropCoordinates)?.takeIf { closestViewHolder ->
            canCreateGroupFromSkillsAtPositions(viewHolder.absoluteAdapterPosition, closestViewHolder.absoluteAdapterPosition)
        }
    }

    private fun canCreateGroupFromSkillsAtPositions(positionOfFirst: Int, positionOfSecond: Int): Boolean {
        val firstSkill = listAdapter.getItemOrNull(positionOfFirst) as? Skill
        val secondSkill = listAdapter.getItemOrNull(positionOfSecond) as? Skill

        return firstSkill != null && secondSkill != null
                && firstSkill.isNotInAGroup && secondSkill.isNotInAGroup
                && firstSkill.canBeInGroupWith(secondSkill)
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
