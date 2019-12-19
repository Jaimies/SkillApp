package com.jdevs.timeo.util

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.ItemsLiveData

typealias LiveDataConstructor = (Query?, (DocumentSnapshot) -> Unit, () -> Unit) -> ItemsLiveData
