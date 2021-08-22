package com.maxpoliakov.skillapp.ui.common

import android.content.Context
import android.graphics.Canvas
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Skill
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import com.maxpoliakov.skillapp.ui.skills.ITEM_TYPE_SKILL_GROUP_FOOTER
import com.maxpoliakov.skillapp.ui.skills.ITEM_TYPE_SKILL_GROUP_HEADER
import com.maxpoliakov.skillapp.ui.skills.SkillGroupFooter
import com.maxpoliakov.skillapp.ui.skills.SkillListAdapter
import com.maxpoliakov.skillapp.util.ui.dp

class CardViewDecoration(
    context: Context,
    private val cornerRadius: Float
) : ItemDecoration() {

    private val cornerShadowPaint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.DITHER_FLAG).apply {
        style = Paint.Style.FILL
        isDither = true
    }

    private val mEdgeShadowPaint = Paint(cornerShadowPaint)
    private var mCornerShadowPath: Path? = null
    private val mShadowSize = 1.dp.toPx(context).toFloat()
    private val mShadowStartColor = ContextCompat.getColor(context, R.color.cardview_shadow_start_color)
    private val mShadowEndColor = ContextCompat.getColor(context, R.color.cardview_shadow_end_color)
    private val headerViewType = ITEM_TYPE_SKILL_GROUP_HEADER

    init {
        buildShadowCorners()
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val bounds = Rect()
        val edgeShadowTop = -cornerRadius - mShadowSize
        val lm = parent.layoutManager
        val padding16dp = 16.dp.toPx(parent.context)
        for (i in 0 until parent.childCount) {
            val save: Int = c.save()

            val child: View = parent.getChildAt(i)
            bounds.set(
                lm!!.getDecoratedLeft(child) + padding16dp,
                lm.getDecoratedTop(child),
                lm.getDecoratedRight(child) - padding16dp,
                lm.getDecoratedBottom(child)
            )
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.absoluteAdapterPosition
            val adapter = parent.adapter!!
            val viewType = adapter.getItemViewType(position)
            if (viewType == headerViewType) {
                // LT
                c.translate(bounds.left + cornerRadius, bounds.top + cornerRadius)
                c.drawPath(mCornerShadowPath!!, cornerShadowPaint)
                c.drawRect(0f, edgeShadowTop, bounds.width() - 2 * cornerRadius, -cornerRadius, mEdgeShadowPaint)

                // RT
                c.rotate(90f)
                c.translate(0f, -bounds.width() + 2 * cornerRadius)
                c.drawPath(mCornerShadowPath!!, cornerShadowPaint)
                c.drawRect(0f, edgeShadowTop, bounds.height() - cornerRadius, -cornerRadius, mEdgeShadowPaint)

                // LBorder
                c.rotate(180f)
                c.translate(-bounds.height().toFloat(), -bounds.width() + 2 * cornerRadius)
                c.drawRect(cornerRadius, edgeShadowTop, bounds.height().toFloat(), -cornerRadius, mEdgeShadowPaint)
            } else if (shouldDrawSideBorders(parent, position)) {
                if (adapter.getItemViewType(position + 1) == ITEM_TYPE_SKILL_GROUP_FOOTER || position >= adapter.itemCount - 1) {
                    bounds.top += padding16dp
                    bounds.bottom += padding16dp

                    // last item before next header
                    c.rotate(180f)
                    c.translate(
                        -bounds.left - bounds.width() + cornerRadius,
                        -bounds.top - bounds.height() + cornerRadius + padding16dp
                    )
                    c.drawPath(mCornerShadowPath!!, cornerShadowPaint)
                    c.drawRect(0f, edgeShadowTop, bounds.width() - 2 * cornerRadius, -cornerRadius, mEdgeShadowPaint)

                    // RT / Right border
                    c.rotate(90f)
                    c.translate(0f, -bounds.width() + 2 * cornerRadius)
                    c.drawPath(mCornerShadowPath!!, cornerShadowPaint)
                    c.drawRect(0f, edgeShadowTop, bounds.height() - cornerRadius, -cornerRadius, mEdgeShadowPaint)

                    // Left border
                    c.rotate(180f)
                    c.translate(-bounds.height().toFloat(), -bounds.width() + 2 * cornerRadius)
                    c.drawRect(
                        cornerRadius,
                        edgeShadowTop,
                        bounds.height().toFloat(),
                        -cornerRadius,
                        mEdgeShadowPaint
                    )
                } else {
                    // Right border
                    c.translate(bounds.left.toFloat(), bounds.top.toFloat())
                    c.rotate(90f)
                    c.translate(0f, -bounds.width() + cornerRadius)
                    c.drawRect(0f, edgeShadowTop, bounds.height().toFloat(), -cornerRadius, mEdgeShadowPaint)

                    // Left border
                    c.rotate(180f)
                    c.translate(-bounds.height().toFloat(), -bounds.width() + 2 * cornerRadius)
                    c.drawRect(0f, edgeShadowTop, bounds.height().toFloat(), -cornerRadius, mEdgeShadowPaint)
                }
            }
            c.restoreToCount(save)
        }
    }

    private fun shouldDrawSideBorders(
        parent: RecyclerView,
        position: Int
    ): Boolean {
        val adapter = parent.adapter!! as SkillListAdapter
        // The first or last item can never need side borders
        if (position <= 0 || position >= adapter.itemCount - 1) return false

        val prevItem = adapter.getItem(position - 1)
        val nextItem = adapter.getItem(position + 1)
        val item = adapter.getItem(position)
        return item is Skill
                && (prevItem is Skill && prevItem.groupId != -1
                || prevItem is SkillGroup
                || nextItem is Skill && nextItem.groupId != -1
                || nextItem is SkillGroupFooter)

    }

    private fun buildShadowCorners() {
        val innerBounds = RectF(-cornerRadius, -cornerRadius, cornerRadius, cornerRadius)
        val outerBounds = RectF(innerBounds)
        outerBounds.inset(-mShadowSize, -mShadowSize)


        if (mCornerShadowPath == null) {
            mCornerShadowPath = Path()
        } else {
            mCornerShadowPath!!.reset()
        }
        mCornerShadowPath!!.fillType = Path.FillType.EVEN_ODD
        mCornerShadowPath!!.moveTo(-cornerRadius, 0f)
        mCornerShadowPath!!.rLineTo(-mShadowSize, 0f)
        // outer arc
        mCornerShadowPath!!.arcTo(outerBounds, 180f, 90f, false)
        // inner arc
        mCornerShadowPath!!.arcTo(innerBounds, 270f, -90f, false)
        mCornerShadowPath!!.close()
        val startRatio = cornerRadius / (cornerRadius + mShadowSize)
        cornerShadowPaint.shader = RadialGradient(
            0f, 0f, cornerRadius + mShadowSize, intArrayOf(
                mShadowStartColor, mShadowStartColor, mShadowEndColor
            ), floatArrayOf(0f, startRatio, 1f),
            Shader.TileMode.CLAMP
        )

        // we offset the content shadowSize/2 pixels up to make it more realistic.
        // this is why edge shadow shader has some extra space
        // When drawing bottom edge shadow, we use that extra space.
        mEdgeShadowPaint.shader = LinearGradient(
            0f,
            -cornerRadius + mShadowSize,
            0f,
            -cornerRadius - mShadowSize,
            intArrayOf(mShadowStartColor, mShadowStartColor, mShadowEndColor),
            floatArrayOf(0f, .5f, 1f),
            Shader.TileMode.CLAMP
        )
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val padding16dp = 16.dp.toPx(parent.context)
        val params = view.layoutParams as RecyclerView.LayoutParams
        val position = params.absoluteAdapterPosition
        val viewType = parent.adapter!!.getItemViewType(position)

        if (viewType == ITEM_TYPE_SKILL_GROUP_FOOTER)
            outRect.set(0, 0, 0, 20.dp.toPx(parent.context))

        outRect.left = padding16dp
        outRect.right = padding16dp
    }
}
