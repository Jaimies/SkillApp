package com.jdevs.timeo.data

interface Mapper<in I, out O> {

    fun map(input: I): O
}
