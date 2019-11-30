package com.jdevs.timeo.util

import com.jdevs.timeo.util.StringConstants.FIRST_RANDOM_SYMBOL_CODE
import com.jdevs.timeo.util.StringConstants.LAST_RANDOM_SYMBOL_CODE

fun randomString(length: Int = 10): String {
    val randomStringBuilder = StringBuilder()
    var tempChar: Char

    for (i in 0..length) {
        tempChar = (FIRST_RANDOM_SYMBOL_CODE..LAST_RANDOM_SYMBOL_CODE).random().toChar()
        randomStringBuilder.append(tempChar)
    }

    return randomStringBuilder.toString()
}
