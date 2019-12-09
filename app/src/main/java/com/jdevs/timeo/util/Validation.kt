package com.jdevs.timeo.util

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS

fun String.isValidEmail() = EMAIL_ADDRESS.matcher(this).matches()

fun String.validatePassword() = when {

    isEmpty() -> EMPTY
    length < PASSWORD_MIN_LENGTH -> TOO_SHORT
    length > PASSWORD_MAX_LENGTH -> TOO_LONG
    else -> VALID
}

fun String.validateEmail() = when {

    isEmpty() -> EMPTY
    !isValidEmail() -> INVALID
    else -> VALID
}

const val EMPTY = 0
const val INVALID = 1
const val TOO_SHORT = 2
const val TOO_LONG = 3
const val VALID = 4

private const val PASSWORD_MIN_LENGTH = 6
private const val PASSWORD_MAX_LENGTH = 25
