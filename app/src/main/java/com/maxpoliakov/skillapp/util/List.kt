@file:JvmName("ListUtil")

package com.maxpoliakov.skillapp.util

fun <T> List<T>?.unshift(element: T) = this?.run { listOf(element) + this }
