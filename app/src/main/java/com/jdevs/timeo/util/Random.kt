package com.jdevs.timeo.util

fun randomString(): String {
    val randomStringBuilder = StringBuilder()
    var tempChar: Char

    for (i in 0..10) {
        tempChar = (32..127).random().toChar()
        randomStringBuilder.append(tempChar)
    }

    return randomStringBuilder.toString()
}
