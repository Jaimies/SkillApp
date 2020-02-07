package com.jdevs.timeo.data.settings

import com.jdevs.timeo.domain.model.User

data class FirestoreUser(
    var name: String = "",
    var email: String = "",
    var activitiesEnabled: Boolean = false
)

fun FirestoreUser.mapToDomain() = User(name, email, activitiesEnabled)
