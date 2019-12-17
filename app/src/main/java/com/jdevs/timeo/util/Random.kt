package com.jdevs.timeo.util

fun randomString(length: Int = 10): String {

    val randomStringBuilder = StringBuilder()
    var tempChar: Char

    for (i in 1..length) {

        tempChar = (FIRST_SYMBOL_CODE..LAST_SYMBOL_CODE).random().toChar()
        randomStringBuilder.append(tempChar)
    }

    return randomStringBuilder.toString()
}

private const val FIRST_SYMBOL_CODE = 32
private const val LAST_SYMBOL_CODE = 127
