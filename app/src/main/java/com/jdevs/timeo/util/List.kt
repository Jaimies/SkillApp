@file:JvmName("ListUtil")

package com.jdevs.timeo.util

fun <T> List<T>?.unshift(element: T) = this?.run { listOf(element) + this }
