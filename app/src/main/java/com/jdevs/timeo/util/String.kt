package com.jdevs.timeo.util

infix fun String.or(other: String): String = if (!isEmpty()) this else other
