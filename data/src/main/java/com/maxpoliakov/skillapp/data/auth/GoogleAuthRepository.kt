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

class GoogleAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val googleSignInClient: GoogleSignInClient,
) : AuthRepository {
    override val currentUser: User?
        get() {
            val googleAccount = GoogleSignIn.getLastSignedInAccount(context) ?: return null
            return User(googleAccount.email.orEmpty())
        }

    override val hasAppDataPermission: Boolean
        get() {
            val account = GoogleSignIn.getLastSignedInAccount(context) ?: return false
            val scope = Scope(DriveScopes.DRIVE_APPDATA)
            return GoogleSignIn.hasPermissions(account, scope)
        }

    override fun signOut() {
        googleSignInClient.signOut()
    }
}
