package com.maxpoliakov.skillapp.shared.extensions

import android.graphics.Rect
import android.view.TouchDelegate
import android.view.View
import com.maxpoliakov.skillapp.shared.Dimension

fun View.increaseTouchAreaBy(size: Dimension) {
    val numberOfPx = size.toPx(context)
    val parent = parent as View
    parent.post {
        val rect = Rect()
        getHitRect(rect)
        rect.top -= numberOfPx
        rect.bottom += numberOfPx
        rect.left -= numberOfPx
        rect.right += numberOfPx
        parent.touchDelegate = TouchDelegate(rect, this)
    }
}
