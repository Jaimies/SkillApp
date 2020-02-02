package com.jdevs.timeo.util

import androidx.core.util.PatternsCompat.EMAIL_ADDRESS

fun isValidEmail(email: String) = EMAIL_ADDRESS.matcher(email).matches()

fun validatePassword(password: String) = when {

    password.isEmpty() -> EMPTY
    password.length < PASSWORD_MIN_LENGTH -> TOO_SHORT
    password.length > PASSWORD_MAX_LENGTH -> TOO_LONG
    else -> VALID
}

fun validateEmail(email: String) = when {

    email.isEmpty() -> EMPTY
    !isValidEmail(email) -> INVALID
    else -> VALID
}

const val EMPTY = 0
const val INVALID = 1
const val TOO_SHORT = 2
const val TOO_LONG = 3
const val VALID = 4

private const val PASSWORD_MIN_LENGTH = 6
private const val PASSWORD_MAX_LENGTH = 25
