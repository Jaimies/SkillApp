package com.jdevs.timeo.common.adapter

interface DataUnit {

    var id: Int
    var documentId: String

    fun getViewType(): Int
}
