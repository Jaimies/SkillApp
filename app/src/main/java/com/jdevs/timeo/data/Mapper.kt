package com.jdevs.timeo.data

import com.jdevs.timeo.common.adapter.ViewItem

interface Mapper<T : ViewItem> {

    fun mapToDomain(): T
}

fun <T : ViewItem> List<Mapper<T>>.mapToDomain() = map { it.mapToDomain() }
