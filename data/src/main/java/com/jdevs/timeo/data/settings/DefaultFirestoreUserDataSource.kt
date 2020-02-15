package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import com.google.firebase.firestore.DocumentReference
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import com.jdevs.timeo.data.firestore.watch
import com.jdevs.timeo.domain.model.User
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

interface UserDataSource {

    fun setPreferenceEnabled(name: String, isEnabled: Boolean)
}

interface FirestoreUserDataSource : UserDataSource {

    val user: LiveData<User>?
}

@Singleton
class DefaultFirestoreUserDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreDataSource(authRepository),
    FirestoreUserDataSource {

    private var userRef: DocumentReference? = null
        get() {
            reset()
            return field
        }

    override val user get() = userRef?.watch(FirestoreUser::mapToDomain)

    override fun setPreferenceEnabled(name: String, isEnabled: Boolean) {

        userRef?.update(name, isEnabled)
    }

    override fun resetRefs(uid: String) {

        userRef = db.document("users/$uid")
    }
}
