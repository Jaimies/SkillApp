package com.maxpoliakov.skillapp.util.charts

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.text.style.LineBackgroundSpan
import android.text.style.ReplacementSpan
import kotlin.math.ceil

class EllipsizeLineSpan : ReplacementSpan(), LineBackgroundSpan {
    private var layoutLeft = 0
    private var layoutRight = 0

    override fun drawBackground(
        c: Canvas, p: Paint,
        left: Int, right: Int,
        top: Int, baseline: Int, bottom: Int,
        text: CharSequence, start: Int, end: Int,
        lnum: Int,
    ) {
        val clipRect = Rect()
        c.getClipBounds(clipRect)
        layoutLeft = clipRect.left
        layoutRight = clipRect.right
    }

    override fun getSize(
        paint: Paint, text: CharSequence?, start: Int, end: Int,
        fm: Paint.FontMetricsInt?,
    ): Int {
        return layoutRight - layoutLeft
    }

    override fun draw(
        canvas: Canvas, text: CharSequence, start: Int, end: Int,
        x: Float, top: Int, y: Int, bottom: Int, paint: Paint,
    ) {
        if (text.fitsOnOneLine(paint, start, end, x)) {
            canvas.drawText(text, start, end, x, y.toFloat(), paint)
        } else {
            canvas.drawEllipsizedText(text, start, end, x, y.toFloat(), paint)
        }
    }

    private fun Canvas.drawEllipsizedText(
        text: CharSequence,
        start: Int,
        end: Int,
        x: Float,
        y: Float,
        paint: Paint,
    ) {
        val end = text.getIndexOfLastCharBeforeEllipsis(start, paint, end, x)
        drawText(text, start, end, x, y, paint)
        drawText("\u2026", x + paint.measureText(text, start, end), y, paint)
    }

    private fun CharSequence.getIndexOfLastCharBeforeEllipsis(start: Int, paint: Paint, end: Int, x: Float): Int {
        val ellipsisWidth = paint.measureText("\u2026")
        return start + paint.breakText(this, start, end, true, layoutRight - x - ellipsisWidth, null)
    }

    private fun CharSequence.fitsOnOneLine(paint: Paint, start: Int, end: Int, x: Float): Boolean {
        val textWidth = ceil(paint.measureText(this, start, end)).toInt()
        return x + textWidth < layoutRight
    }
}