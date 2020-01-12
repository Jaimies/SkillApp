package com.jdevs.timeo.data.settings

import com.jdevs.timeo.data.Mapper

data class User(val name: String, val email: String, val activitiesEnabled: Boolean)

data class FirestoreUser(
    var name: String = "",
    var email: String = "",
    var activitiesEnabled: Boolean = false
) : Mapper<User> {

    override fun mapToDomain() = User(name, email, activitiesEnabled)
}
