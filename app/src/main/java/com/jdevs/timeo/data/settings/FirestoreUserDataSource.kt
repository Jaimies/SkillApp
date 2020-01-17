package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.monitor
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

interface UserDataSource {

    fun getUser(): LiveData<User>?

    fun updateUserPreferences(field: String, value: Any)
}

@Singleton
class FirestoreUserDataSource @Inject constructor(
    authRepository: AuthRepository,
    private val mapper: FirestoreUserMapper
) :
    FirestoreDataSource(authRepository),
    UserDataSource {

    private var userRef: DocumentReference? = null
        get() = field.safeAccess()

    override fun getUser() = userRef?.monitor(FirestoreUser::class, mapper)

    override fun updateUserPreferences(field: String, value: Any) {

        userRef?.update(field, value)
    }

    override fun resetRefs(uid: String) {

        userRef = db.document("users/$uid")
    }
}
