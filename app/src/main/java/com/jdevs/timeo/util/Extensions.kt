package com.jdevs.timeo.util

fun <T> lazyUnsynchronized(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)