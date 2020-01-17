package com.jdevs.timeo.data.settings

import com.jdevs.timeo.data.Mapper
import javax.inject.Inject

class FirestoreUserMapper @Inject constructor() : Mapper<FirestoreUser, User> {

    override fun map(input: FirestoreUser) = input.run { User(name, email, activitiesEnabled) }
}
