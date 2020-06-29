package com.jdevs.timeo.util.ui

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.jdevs.timeo.R

private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.fragment_open_enter)
    .setExitAnim(R.anim.fragment_open_exit)
    .setPopEnterAnim(R.anim.fragment_close_enter)
    .setPopExitAnim(R.anim.fragment_close_exit)
    .build()

fun NavController.navigateAnimated(@IdRes resId: Int) = navigate(resId, null, navOptions)
fun NavController.navigateAnimated(directions: NavDirections) = navigate(directions, navOptions)
