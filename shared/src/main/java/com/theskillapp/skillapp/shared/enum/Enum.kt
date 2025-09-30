package com.theskillapp.skillapp.shared.enum

inline fun <reified T : Enum<T>> enumHasValue(name: String): Boolean {
    return enumValues<T>().any { it.name == name }
}
