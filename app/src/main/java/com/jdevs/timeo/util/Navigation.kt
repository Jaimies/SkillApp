package com.jdevs.timeo.util

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavOptions

fun NavController.navigateAnimated(@IdRes resId: Int) {

    val options = NavOptions.Builder()
        .setEnterAnim(R.anim.fragment_open_enter)
        .setExitAnim(R.anim.fragment_open_exit)
        .setPopEnterAnim(R.anim.fragment_close_enter)
        .setPopExitAnim(R.anim.fragment_close_exit)
        .build()

    navigate(resId, null, options)
}
