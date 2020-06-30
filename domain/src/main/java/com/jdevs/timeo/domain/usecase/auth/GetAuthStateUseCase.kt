package com.jdevs.timeo.domain.usecase.auth

import com.jdevs.timeo.domain.repository.AuthRepository
import javax.inject.Inject

class GetAuthStateUseCase @Inject constructor(private val authRepository: AuthRepository) {

    val isSignedIn get() = authRepository.isSignedIn
}
