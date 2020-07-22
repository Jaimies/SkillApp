package com.jdevs.timeo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeAuthRepository @Inject constructor() : AuthRepository {
    override val isSignedIn: LiveData<Boolean> get() = _isSignedIn
    private val _isSignedIn = MutableLiveData(false)
    override val uid = MutableLiveData("")

    fun signIn() {
        _isSignedIn.value = true
    }

    override fun signOut() {
        _isSignedIn.value = false
    }

    override suspend fun createUser(email: String, password: String): SignUpResult {
        return signIn().let { SignUpResult.Success }
    }

    override suspend fun signIn(email: String, password: String): SignInResult {
        return signIn().let { SignInResult.Success }
    }

    override suspend fun signInWithGoogle(accountTask: Task<GoogleSignInAccount>): GoogleSignInResult {
        return signIn().let { GoogleSignInResult.Success }
    }
}
