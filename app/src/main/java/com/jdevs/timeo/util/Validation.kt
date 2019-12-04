package com.jdevs.timeo.util

import com.jdevs.timeo.R
import com.jdevs.timeo.util.AuthConstants.PASSWORD_MAX_LENGTH
import com.jdevs.timeo.util.AuthConstants.PASSWORD_MIN_LENGTH

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.validatePassword(): Int = when {
    isEmpty() -> R.id.ERROR_EMPTY
    length < PASSWORD_MIN_LENGTH -> R.id.ERROR_TOO_SHORT
    length > PASSWORD_MAX_LENGTH -> R.id.ERROR_TOO_LONG
    else -> R.id.RESULT_VALID
}

fun String.validateEmail(): Int = when {
    isEmpty() -> R.id.ERROR_EMPTY
    !isValidEmail() -> R.id.ERROR_INVALID
    else -> R.id.RESULT_VALID
}
