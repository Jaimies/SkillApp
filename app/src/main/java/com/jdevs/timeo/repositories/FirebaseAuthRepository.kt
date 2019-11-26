package com.jdevs.timeo.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.GoogleAuthProvider
import com.jdevs.timeo.R
import com.jdevs.timeo.data.AuthState
import com.jdevs.timeo.util.TAG
import com.jdevs.timeo.util.logOnFailure

class FirebaseAuthRepository {

    private val auth by lazy { FirebaseAuth.getInstance() }

    fun createAccount(email: String, password: String): LiveData<AuthState> {

        val state = MutableLiveData(AuthState(R.id.AUTH_STATE_IN_PROGRESS))

        val credential = EmailAuthProvider.getCredential(email, password)

        auth.currentUser!!.linkWithCredential(credential)
            .addOnCompleteListener {

                state.value = AuthState(R.id.AUTH_STATE_FINISHED)
            }
            .addOnSuccessListener {

                state.value = AuthState(R.id.AUTH_STATE_SUCCESSFUL)
            }

            .addOnFailureListener { exception ->

                state.value = AuthState(R.id.AUTH_STATE_FAILED, exception)
            }

        return state
    }

    fun signIn(email: String, password: String): LiveData<AuthState> {

        val state = MutableLiveData(AuthState(R.id.AUTH_STATE_IN_PROGRESS))

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {

                state.value = AuthState(R.id.AUTH_STATE_FINISHED)
            }
            .addOnSuccessListener {

                if (auth.currentUser?.isAnonymous == true) {

                    auth.currentUser?.delete()?.logOnFailure("Failed to delete user data")
                }

                state.value = AuthState(R.id.AUTH_STATE_SUCCESSFUL)
            }
            .addOnFailureListener { error ->

                state.value = AuthState(R.id.AUTH_STATE_FAILED, error)
            }

        return state
    }

    fun linkGoogleAccount(account: GoogleSignInAccount): LiveData<Int> {

        val state = MutableLiveData(R.id.AUTH_STATE_IN_PROGRESS)

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        val user = auth.currentUser

        if (user != null && user.isAnonymous) {

            user.linkWithCredential(credential)
                .addOnSuccessListener {

                    state.value = R.id.AUTH_STATE_SUCCESSFUL
                }
                .addOnFailureListener { linkCredentialException ->

                    if (linkCredentialException is FirebaseAuthUserCollisionException) {
                        signInWithGoogle(state, credential)

                        return@addOnFailureListener
                    }

                    Log.w(TAG, "signInWithCredential:failure", linkCredentialException)
                }
        } else {

            signInWithGoogle(state, credential)
        }

        return state
    }

    private fun signInWithGoogle(state: MutableLiveData<Int>, credential: AuthCredential) {

        if (auth.currentUser?.isAnonymous == true) {

            auth.currentUser?.delete()?.logOnFailure("Failed to delete the anonymous user")
        }

        auth.signInWithCredential(credential)

            .addOnSuccessListener {

                state.value = R.id.AUTH_STATE_SUCCESSFUL
            }
            .addOnFailureListener { exception ->

                state.value = R.id.AUTH_STATE_FAILED
                Log.w(TAG, "Failed to sign in with google", exception)
            }
    }
}
