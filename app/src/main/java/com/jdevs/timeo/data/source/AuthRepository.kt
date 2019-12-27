package com.jdevs.timeo.data.source

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.util.await

object AuthRepository : UserManager {

    override val isUserSignedIn
        get() = auth.currentUser != null

    val uid
        get() = auth.currentUser?.uid

    private val auth = FirebaseAuth.getInstance()

    suspend fun createAccount(email: String, password: String): AuthResult {

        return auth.createUserWithEmailAndPassword(email, password).await()
    }

    suspend fun signIn(email: String, password: String): AuthResult {

        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun linkGoogleAccount(account: GoogleSignInAccount): AuthResult {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        return auth.signInWithCredential(credential).await()
    }

    fun signOut() = auth.signOut()
}
