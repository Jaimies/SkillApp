package com.jdevs.timeo.data.auth

import await
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.util.logOnFailure

object AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun createAccount(email: String, password: String): AuthResult {

        val credential = EmailAuthProvider.getCredential(email, password)

        return try {

            auth.currentUser!!.linkWithCredential(credential).await()
        } catch (e: KotlinNullPointerException) {

            auth.createUserWithEmailAndPassword(email, password).await()
        }
    }

    suspend fun signIn(email: String, password: String): AuthResult {

        deleteCurrentUser()

        return auth.signInWithEmailAndPassword(email, password).await()
    }

    @Suppress("TooGenericExceptionCaught")
    suspend fun linkGoogleAccount(account: GoogleSignInAccount): AuthResult {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)

        try {

            return auth.currentUser!!.linkWithCredential(credential).await()
        } catch (exception: Exception) {

            if (exception is FirebaseAuthUserCollisionException || exception is KotlinNullPointerException) {

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
