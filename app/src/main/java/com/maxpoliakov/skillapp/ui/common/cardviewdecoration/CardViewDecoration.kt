package com.maxpoliakov.skillapp.ui.common.cardviewdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Build
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.ui.Dp
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue

abstract class CardViewDecoration : ItemDecoration() {
    private val Context.size16dp get() = 16.dp.toPx(this).toFloat()
    private val Context.shadowColor get() = ContextCompat.getColor(this, R.color.cardview_shadow_start_color)

    private val Context.fillPaint
        get() = Paint().also {
            it.color = getColorAttributeValue(R.attr.cardViewBackground)
            it.setShadowLayer(4.dp.toPx(this).toFloat(), 0f, 1.dp.toPx(this).toFloat(), shadowColor)
        }

    protected abstract val cardFooterViewType: Int

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val drawnGroupIds = mutableListOf<Int>()

        for (i in 0 until parent.childCount) {
            val viewHolder = parent.getChildViewHolder(parent.getChildAt(i))

            if (viewHolder !is PartOfFakeCardView || viewHolder.cardId == -1
                || drawnGroupIds.contains(viewHolder.cardId)) continue

            drawnGroupIds.add(viewHolder.cardId)

            val lastViewHolder = parent.findLastViewHolderInGroup(viewHolder.cardId) ?: continue
            c.drawCardView(parent, lastViewHolder, viewHolder)
        }
    }

    private fun Canvas.drawCardView(
        parent: RecyclerView,
        lastViewHolder: RecyclerView.ViewHolder,
        firstViewHolder: RecyclerView.ViewHolder
    ) = parent.context.run {
        val rect = getCardViewRect(firstViewHolder, lastViewHolder)

        if (Build.VERSION.SDK_INT < 28)
            drawFakeShadow(shadowColor, rect, parent.context)

        drawRoundRect(rect, size16dp, size16dp, fillPaint)
    }

    private fun getBottomOffset(typeOfLastViewHolder: Int): Dp {
        if (typeOfLastViewHolder != cardFooterViewType) {
            // This is necessary to prevent the rounded edges from showing when
            // the last item visible is not the last item in the group
            return 16.dp
        }

        return 0.dp
    }

    private fun Context.getCardViewRect(
        firstViewHolder: RecyclerView.ViewHolder,
        lastViewHolder: RecyclerView.ViewHolder,
    ): RectF {
        val bottomOffset = getBottomOffset(lastViewHolder.itemViewType).toPx(this)

        return RectF(
            size16dp,
            firstViewHolder.itemView.top.toFloat(),
            firstViewHolder.itemView.right.toFloat(),
            lastViewHolder.itemView.bottom.toFloat() + bottomOffset,
        )
    }

    private fun Canvas.drawFakeShadow(
        shadowColor: Int,
        rect: RectF,
        context: Context,
    ) {
        val shadowPaint = Paint().apply {
            color = shadowColor
        }

        val shadowRect = RectF(rect).apply {
            top -= 1.dp.toPx(context)
            left -= 1.dp.toPx(context)
            right += 1.dp.toPx(context)
            bottom += 1.5f.dp.toPx(context)
        }

        drawRoundRect(shadowRect, 16.dp.toPx(context).toFloat(), 16.dp.toPx(context).toFloat(), shadowPaint)
    }

    private fun RecyclerView.findLastViewHolderInGroup(cardId: Int): RecyclerView.ViewHolder? {
        for (i in childCount - 1 downTo 0) {
            val holder = getChildViewHolder(getChildAt(i))
            if (holder is PartOfFakeCardView && holder.cardId == cardId) return holder
        }

        return null
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)

        val padding16dp = 16.dp.toPx(parent.context)
        val params = view.layoutParams as RecyclerView.LayoutParams
        val position = params.absoluteAdapterPosition
        val viewType = parent.adapter!!.getItemViewType(position)

        if (viewType == cardFooterViewType)
            outRect.set(0, 0, 0, 24.dp.toPx(parent.context))

        outRect.left = padding16dp
        outRect.right = padding16dp
    }
}
