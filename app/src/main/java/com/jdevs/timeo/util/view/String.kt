@file:JvmName("StringUtil")

package com.jdevs.timeo.util.view

import android.content.Context
import androidx.annotation.StringRes

fun Context.getStringSafe(@StringRes resId: Int): String? {
    return if (resId != -1) getString(resId) else null
}