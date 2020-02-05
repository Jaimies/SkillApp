package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

interface UserDataSource {

    fun enablePreference(name: String, isEnabled: Boolean)
}

interface FirestoreUserDataSource : UserDataSource {

    fun getUser(): LiveData<User>?
}

@Singleton
class DefaultFirestoreUserDataSource @Inject constructor(
    authRepository: AuthRepository,
    private val mapper: FirestoreUserMapper
) :
    FirestoreDataSource(authRepository),
    FirestoreUserDataSource {

    private var userRef: DocumentReference? = null
        get() = field.safeAccess()

    override fun getUser() = userRef?.watch(FirestoreUser::class, mapper)

    override fun enablePreference(name: String, isEnabled: Boolean) {

        userRef?.update(name, isEnabled)
    }

    override fun resetRefs(uid: String) {

        userRef = db.document("users/$uid")
    }
}
