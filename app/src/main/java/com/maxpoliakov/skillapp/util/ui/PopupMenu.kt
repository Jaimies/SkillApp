package com.maxpoliakov.skillapp.util.ui

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.annotation.MenuRes

fun showPopupMenu(context: Context, view: View, @MenuRes menuId: Int): PopupMenu {
    val popup = PopupMenu(context, view, Gravity.END)

    popup.menuInflater.inflate(menuId, popup.menu)
    popup.show()
    return popup
}
