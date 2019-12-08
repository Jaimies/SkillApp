package com.jdevs.timeo.api.repository.auth

import await
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.util.logOnFailure

object AuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

    suspend fun createAccount(email: String, password: String): AuthResult {

        val credential = EmailAuthProvider.getCredential(email, password)

        return auth.currentUser!!.linkWithCredential(credential).await()
    }

    suspend fun signIn(email: String, password: String): AuthResult {

        deleteCurrentUser()

        return auth.signInWithEmailAndPassword(email, password).await()
    }

    suspend fun linkGoogleAccount(account: GoogleSignInAccount): AuthResult {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        try {

            return auth.currentUser!!.linkWithCredential(credential).await()
        } catch (exception: FirebaseAuthException) {

            if (exception is FirebaseAuthUserCollisionException) {

                return signInWithGoogle(credential).await()
            }

            throw exception
        }
    }

    suspend fun signInAnonymously() {

        auth.signInAnonymously().await()
    }

    fun signOut() {

        auth.signOut()
    }

    private fun signInWithGoogle(credential: AuthCredential): Task<AuthResult> {

        deleteCurrentUser()

        return auth.signInWithCredential(credential)
    }

    private fun deleteCurrentUser() {

        if (auth.currentUser?.isAnonymous == true) {

            auth.currentUser?.delete()?.logOnFailure("Failed to delete the anonymous user")
        }
    }
}
