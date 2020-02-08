package com.jdevs.timeo.data.settings

import com.jdevs.timeo.domain.model.User

data class FirestoreUser(
    val name: String = "",
    val email: String = "",
    val activitiesEnabled: Boolean = false
)

fun FirestoreUser.mapToDomain() = User(name, email, activitiesEnabled)
