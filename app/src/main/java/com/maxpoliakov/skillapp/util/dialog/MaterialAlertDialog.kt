package com.maxpoliakov.skillapp.util.dialog

import android.content.Context
import androidx.annotation.StringRes
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maxpoliakov.skillapp.R

fun Context.showDialog(
    @StringRes messageTextId: Int,
    @StringRes confirmButtonTextId: Int,
    onConfirmed: () -> Unit
) {
    MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_SkillApp_AlertDialog)
        .setMessage(messageTextId)
        .setPositiveButton(confirmButtonTextId) { _, _ -> onConfirmed() }
        .setNegativeButton(R.string.cancel, null)
        .show()
}
