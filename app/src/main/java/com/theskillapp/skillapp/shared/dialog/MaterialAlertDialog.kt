package com.theskillapp.skillapp.shared.dialog

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.theskillapp.skillapp.R

fun Context.showDialog(
    @StringRes titleTextId: Int?,
    @StringRes messageTextId: Int,
    @StringRes confirmButtonTextId: Int,
    onConfirmed: () -> Unit
) {
    val builder = MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_SkillApp_AlertDialog)
        .setMessage(messageTextId)
        .setPositiveButton(confirmButtonTextId) { _, _ -> onConfirmed() }
        .setNegativeButton(R.string.cancel, null)

    if (titleTextId != null)
        builder.setTitle(titleTextId)

    builder.show()
}

fun Context.showDialog(
    @StringRes messageTextId: Int,
    @StringRes confirmButtonTextId: Int,
    onConfirmed: () -> Unit
) {
    showDialog(null, messageTextId, confirmButtonTextId, onConfirmed)
}
