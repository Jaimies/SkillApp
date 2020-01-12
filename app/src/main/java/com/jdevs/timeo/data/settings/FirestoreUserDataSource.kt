package com.jdevs.timeo.data.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentReference
import com.jdevs.timeo.data.auth.AuthRepository
import com.jdevs.timeo.data.firestore.FirestoreDataSource
import javax.inject.Inject

interface UserDataSource {

    fun getUser(): LiveData<User>

    fun updateUserPreferences(field: String, value: Any)
}

class FirestoreUserDataSource @Inject constructor(authRepository: AuthRepository) :
    FirestoreDataSource(authRepository),
    UserDataSource {

    private var userRef: DocumentReference? = null
        get() = field.safeAccess()

    override fun getUser(): LiveData<User> {

        val liveData = MutableLiveData<User>()
        userRef?.addSnapshotListener { documentSnapshot, _ ->

            documentSnapshot?.run {

                val newUser = toObject(FirestoreUser::class.java)
                newUser?.let { liveData.value = it.mapToDomain() }
            }
        }

        return liveData
    }

    override fun updateUserPreferences(field: String, value: Any) {

        userRef?.update(field, value)
    }

    override fun resetRefs(uid: String) {

        userRef = db.document("users/$uid")
    }
}
