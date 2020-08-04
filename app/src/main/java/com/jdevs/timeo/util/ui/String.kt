@file:JvmName("StringUtil")

package com.jdevs.timeo.util.ui

import android.content.Context
import androidx.annotation.StringRes

fun Context.getStringOrNull(@StringRes resId: Int): String? {
    return runCatching { getString(resId) }.getOrNull()
}
