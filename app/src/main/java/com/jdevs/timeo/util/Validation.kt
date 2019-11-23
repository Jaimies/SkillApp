package com.jdevs.timeo.util

import com.jdevs.timeo.R

fun String.isValidEmail(): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun CharSequence.isValidEmail(): Boolean {
    return this.toString().isValidEmail()
}

fun String.validatePassword(): Int {
    return when {
        isEmpty() -> R.id.ERROR_EMPTY
        length < 6 -> R.id.ERROR_TOO_SHORT
        length > 25 -> R.id.ERROR_TOO_LONG
        else -> R.id.RESULT_VALID
    }
}

fun String.validateEmail(): Int {
    return when {
        isEmpty() -> R.id.ERROR_EMPTY
        !isValidEmail() -> R.id.ERROR_INVALID
        else -> R.id.RESULT_VALID
    }
}
