package com.jdevs.timeo.domain.usecase.auth

import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend fun run(email: String, password: String) =
        authRepository.createUser(email, password)
}
