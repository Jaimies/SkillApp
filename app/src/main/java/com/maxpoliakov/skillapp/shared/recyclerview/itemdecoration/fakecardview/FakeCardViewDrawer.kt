package com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.Dimension.Companion.dp
import com.maxpoliakov.skillapp.shared.extensions.getColorAttributeValue

class FakeCardViewDrawer(
    private val parent: RecyclerView,
    private val canvas: Canvas,
    private val firstViewHolder: RecyclerView.ViewHolder,
    private val lastViewHolder: RecyclerView.ViewHolder,
) {
    private val context get() = parent.context
    private val size16dp = 16.dp.toPx(context).toFloat()

    private val shadowColor = ContextCompat.getColor(context, R.color.cardview_shadow_start_color)

    private val fillPaint = Paint().also {
        it.color = context.getColorAttributeValue(R.attr.cardViewBackground)
        it.setShadowLayer(4.dp.toPx(context).toFloat(), 0f, 1.dp.toPx(context).toFloat(), shadowColor)
    }

    fun draw() {
        val rect = getRect()

        if (Build.VERSION.SDK_INT < 28)
            drawFakeShadow(shadowColor, rect)

        canvas.drawRoundRect(rect, size16dp, size16dp, fillPaint)
    }

    private fun getRect(): RectF {
        return getRectThatWrapsTheViewHolders().apply {
            pushTopEdgeOffScreenIfFirstItemIsNotHeader()
            pushBottomEdgeOffScreenIfLastItemIsNotFooter()
        }
    }

    // Prevents rounded corners from being shown on the top
    // if the first item visible is not the first item in the group
    private fun RectF.pushTopEdgeOffScreenIfFirstItemIsNotHeader() {
        if (firstViewHolder.itemViewType != FakeCardViewDecoration.HEADER_VIEW_TYPE) {
            top -= 16.dp.toPx(context)
        }
    }

    // Prevents rounded corners from being shown on the bottom
    // if the last item visible is not the last item in the group
    private fun RectF.pushBottomEdgeOffScreenIfLastItemIsNotFooter() {
        if (lastViewHolder.itemViewType != FakeCardViewDecoration.FOOTER_VIEW_TYPE) {
            bottom += 16.dp.toPx(context)
        }
    }

    private fun getRectThatWrapsTheViewHolders() = RectF(
        parent.paddingLeft.toFloat(),
        firstViewHolder.itemView.top.toFloat(),
        (parent.right - parent.paddingRight).toFloat(),
        lastViewHolder.itemView.bottom.toFloat(),
    )

    private fun drawFakeShadow(shadowColor: Int, rect: RectF) {
        val shadowPaint = Paint().apply {
            color = shadowColor
        }

        val shadowRect = RectF(rect).apply {
            top -= 1.dp.toPx(context)
            left -= 1.dp.toPx(context)
            right += 1.dp.toPx(context)
            bottom += 1.5f.dp.toPx(context)
        }

        canvas.drawRoundRect(shadowRect, 16.dp.toPx(context).toFloat(), 16.dp.toPx(context).toFloat(), shadowPaint)
    }
}
