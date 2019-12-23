package com.jdevs.timeo.common.adapter

import androidx.room.Ignore
import com.google.firebase.firestore.Exclude

interface ViewType {

    var id: Int
    var documentId: String

    @Exclude
    @Ignore
    fun getViewType(): Int
}
