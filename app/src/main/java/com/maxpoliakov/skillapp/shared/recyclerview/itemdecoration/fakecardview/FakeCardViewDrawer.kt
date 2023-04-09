package com.maxpoliakov.skillapp.shared.recyclerview.itemdecoration.fakecardview

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.shared.Dimension
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
        val bottomOffset = getBottomOffset(lastViewHolder.itemViewType).toPx(context)

        return RectF(
            parent.paddingLeft.toFloat(),
            firstViewHolder.itemView.top.toFloat(),
            (parent.right - parent.paddingRight).toFloat(),
            lastViewHolder.itemView.bottom.toFloat() + bottomOffset,
        )
    }

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

    private fun getBottomOffset(typeOfLastViewHolder: Int): Dimension {
        if (typeOfLastViewHolder != FakeCardViewDecoration.FOOTER_VIEW_TYPE) {
            // This is necessary to prevent the rounded edges from showing when
            // the last item visible is not the last item in the group
            return 16.dp
        }

        return 0.dp
    }
}
