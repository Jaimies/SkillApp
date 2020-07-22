package com.jdevs.timeo.domain.usecase.auth

import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(private val authRepository: AuthRepository) {
    operator fun invoke() = authRepository.signOut()
}
