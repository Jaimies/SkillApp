package com.maxpoliakov.skillapp.shared.extensions

import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import com.maxpoliakov.skillapp.R

private val navOptions = NavOptions.Builder()
    .setEnterAnim(R.anim.slide_in_right)
    .setExitAnim(R.anim.slide_out_left)
    .setPopEnterAnim(R.anim.slide_in_left)
    .setPopExitAnim(R.anim.slide_out_right)
    .build()

fun NavController.navigateAnimated(@IdRes resId: Int) = navigate(resId, null, navOptions)
fun NavController.navigateAnimated(directions: NavDirections) = navigate(directions, navOptions)
