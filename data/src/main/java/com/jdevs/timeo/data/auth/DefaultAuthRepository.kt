package com.jdevs.timeo.data.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes.SIGN_IN_CANCELLED
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.CommonStatusCodes.NETWORK_ERROR
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.domain.model.result.GoogleSignInResult
import com.jdevs.timeo.domain.model.result.SignInResult
import com.jdevs.timeo.domain.model.result.SignUpResult
import com.jdevs.timeo.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultAuthRepository @Inject constructor() : AuthRepository {

    override val isSignedIn
        get() = auth.currentUser != null

    override val uid
        get() = auth.currentUser?.uid

    private val auth = FirebaseAuth.getInstance()

    override suspend fun createUser(email: String, password: String): SignUpResult {

        return try {

            auth.createUserWithEmailAndPassword(email, password).await()
            SignUpResult.Success
        } catch (exception: FirebaseAuthException) {
            when (exception) {
                is FirebaseAuthInvalidCredentialsException -> SignUpResult.InvalidEmail
                is FirebaseAuthWeakPasswordException -> SignUpResult.WeakPassword
                is FirebaseAuthUserCollisionException -> SignUpResult.UserAlreadyExists
                else -> SignUpResult.InternalError
            }
        }
    }

    override suspend fun signIn(email: String, password: String): SignInResult {

        return try {

            auth.signInWithEmailAndPassword(email, password).await()
            SignInResult.Success
        } catch (exception: FirebaseAuthException) {
            when (exception) {
                is FirebaseAuthInvalidUserException -> SignInResult.NoSuchUser
                is FirebaseAuthInvalidCredentialsException -> SignInResult.IncorrectPassword
                else -> SignInResult.InternalError
            }
        }
    }

    @Suppress("TooGenericExceptionCaught")
    override suspend fun signInWithGoogle(accountTask: Task<GoogleSignInAccount>): GoogleSignInResult {

        return try {

            val account = accountTask.getResult(ApiException::class.java)!!
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            auth.signInWithCredential(credential).await()
            GoogleSignInResult.Success
        } catch (exception: Exception) {

            when (exception) {
                is FirebaseAuthInvalidUserException -> GoogleSignInResult.UserAccountDisabled

                is ApiException -> when (exception.statusCode) {
                    NETWORK_ERROR -> GoogleSignInResult.NetworkFailure
                    SIGN_IN_CANCELLED -> GoogleSignInResult.Cancelled
                    else -> GoogleSignInResult.InternalError
                }

                else -> GoogleSignInResult.InternalError
            }
        }
    }

    override fun signOut() = auth.signOut()
}
