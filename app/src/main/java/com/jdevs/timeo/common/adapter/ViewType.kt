package com.jdevs.timeo.common.adapter

interface ViewType {

    var id: Int
    var documentId: String

    fun getViewType(): Int
}
