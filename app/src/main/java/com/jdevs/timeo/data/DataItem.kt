package com.jdevs.timeo.data

interface DataItem {

    var id: Int
    var documentId: String

    fun getViewType(): Int
}
