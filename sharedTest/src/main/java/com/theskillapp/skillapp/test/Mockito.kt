package com.theskillapp.skillapp.test

import org.mockito.Mockito

fun <T> any(): T {
    Mockito.any<T>()
    return null as T
}
