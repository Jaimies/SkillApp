package com.jdevs.timeo.data.settings

data class User(val name: String, val email: String, val activitiesEnabled: Boolean)

data class FirestoreUser(
    var name: String = "",
    var email: String = "",
    var activitiesEnabled: Boolean = false
)
