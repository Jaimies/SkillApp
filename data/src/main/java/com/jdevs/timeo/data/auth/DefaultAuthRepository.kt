package com.jdevs.timeo.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAuthRepository @Inject constructor() : AuthRepository {

    override val isUserSignedIn
        get() = auth.currentUser != null

    override val uid
        get() = auth.currentUser?.uid

    private val auth = FirebaseAuth.getInstance()

    override suspend fun createAccount(email: String, password: String) {

        auth.createUserWithEmailAndPassword(email, password).await()
    }

    override suspend fun signIn(email: String, password: String) {

        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun linkGoogleAccount(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).await()
    }

    override fun signOut() = auth.signOut()
}
