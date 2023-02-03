package com.maxpoliakov.skillapp.domain.model

interface Change<T> {
    fun apply(value: T): T
}
