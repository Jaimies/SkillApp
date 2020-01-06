package com.jdevs.timeo.data

import com.jdevs.timeo.common.adapter.ViewItem

interface Mapper<T : ViewItem> {

    fun mapToDomain(): T
}
