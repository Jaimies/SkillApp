package com.jdevs.timeo.common.adapter

import androidx.room.Ignore
import com.google.firebase.firestore.Exclude

interface ViewType {

    @Exclude
    @Ignore
    fun getViewType(): Int
}
