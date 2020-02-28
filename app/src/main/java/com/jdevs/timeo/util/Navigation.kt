package com.jdevs.timeo.util

import android.os.Bundle
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

private val navOptions
    get() = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_open_enter)
        .setExitAnim(R.anim.fragment_open_exit)
        .setPopEnterAnim(R.anim.fragment_close_enter)
        .setPopExitAnim(R.anim.fragment_close_exit)
        .build()

fun NavController.navigateAnimated(@IdRes resId: Int) {

    navigate(resId, null, navOptions)
}

fun NavController.navigateAnimated(@IdRes resId: Int, args: Bundle) {

    navigate(resId, args, navOptions)
}
