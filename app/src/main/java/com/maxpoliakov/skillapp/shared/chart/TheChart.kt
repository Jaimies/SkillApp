package com.maxpoliakov.skillapp.shared.chart

interface TheChart<T> {
    fun update(data: T?)
}
