@file:JvmName("ListUtil")

package com.jdevs.timeo.util

fun <T> MutableList<T>?.unshift(element: T) = this?.apply { add(0, element) }
