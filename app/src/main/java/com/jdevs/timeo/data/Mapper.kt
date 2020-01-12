package com.jdevs.timeo.data

interface Mapper<T : Any> {

    fun mapToDomain(): T
}

fun <T : Any> List<Mapper<T>>.mapToDomain() = map { it.mapToDomain() }
