package com.maxpoliakov.skillapp.ui.common.cardviewdecoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.util.ui.Dp
import com.maxpoliakov.skillapp.util.ui.dp
import com.maxpoliakov.skillapp.util.ui.getColorAttributeValue

class FakeCardViewDrawer(
    private val context: Context,
    private val canvas: Canvas,
    private val firstViewHolder: RecyclerView.ViewHolder,
    private val lastViewHolder: RecyclerView.ViewHolder,
    private val cardFooterViewType: Int,
) {
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
            size16dp,
            firstViewHolder.itemView.top.toFloat(),
            firstViewHolder.itemView.right.toFloat(),
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

    private fun getBottomOffset(typeOfLastViewHolder: Int): Dp {
        if (typeOfLastViewHolder != cardFooterViewType) {
            // This is necessary to prevent the rounded edges from showing when
            // the last item visible is not the last item in the group
            return 16.dp
        }

        return 0.dp
    }
}
