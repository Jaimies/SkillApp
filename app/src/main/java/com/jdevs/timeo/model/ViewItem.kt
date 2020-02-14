package com.jdevs.timeo.model

interface ViewItem {

    val id: String
    val viewType: Int
}

object ViewType {

    const val LOADING = 0
    const val ACTIVITY = 1
    const val RECORD = 2
    const val PROJECT = 3
}
