package com.jdevs.timeo.domain.usecase.auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject

class SignInUseCase @Inject constructor(private val authRepository: AuthRepository) {

    suspend fun signIn(email: String, password: String) = authRepository.signIn(email, password)
    suspend fun signInWithGoogle(account: GoogleSignInAccount) =
        authRepository.signInWithGoogle(account)
}
