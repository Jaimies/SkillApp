package com.jdevs.timeo.data.settings

import androidx.annotation.Keep
import com.jdevs.timeo.domain.model.User

@Keep
data class FirestoreUser(
    val name: String = "",
    val email: String = "",
    val activitiesEnabled: Boolean = false
)

fun FirestoreUser.mapToDomain() = User(name, email, activitiesEnabled)
