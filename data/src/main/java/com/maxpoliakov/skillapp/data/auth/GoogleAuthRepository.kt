package com.maxpoliakov.skillapp.data.auth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.Scope
import com.google.api.services.drive.DriveScopes
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient,
) : AuthRepository {
    private val signInListeners = mutableListOf<AuthRepository.SignInListener>()
    private val signOutListeners = mutableListOf<AuthRepository.SignOutListener>()

    override val currentUser: User?
        get() {
            val googleAccount = GoogleSignIn.getLastSignedInAccount(context) ?: return null
            val appDataScope = Scope(DriveScopes.DRIVE_APPDATA)

            return User(
                email = googleAccount.email.orEmpty(),
                hasAppDataPermission = GoogleSignIn.hasPermissions(googleAccount, appDataScope),
            )
        }

    override fun addSignInListener(listener: AuthRepository.SignInListener) {
        signInListeners.add(listener)
    }

    override fun addSignOutListener(listener: AuthRepository.SignOutListener) {
        signOutListeners.add(listener)
    }

    override fun removeSignInListener(listener: AuthRepository.SignInListener) {
        signInListeners.remove(listener)
    }

    override fun removeSignOutListener(listener: AuthRepository.SignOutListener) {
        signOutListeners.remove(listener)
    }

    override fun reportSignIn() {
        signInListeners.forEach(AuthRepository.SignInListener::onSignedIn)
    }

    override fun signOut() {
        googleSignInClient.signOut()
        signOutListeners.forEach(AuthRepository.SignOutListener::onSignedOut)
    }
}
