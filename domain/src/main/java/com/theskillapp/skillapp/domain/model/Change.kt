package com.theskillapp.skillapp.domain.model

interface Change<T> {
    fun apply(value: T): T
}
