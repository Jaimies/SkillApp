package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEvent<Nothing>()
    val signIn: LiveData<Nothing> get() = _signIn

    fun notifySignedIn() {
        _currentUser.value = authRepository.currentUser
    }

    fun signOut() {
        authRepository.signOut()
        _currentUser.value = null
    }

    fun signIn() {
        _signIn.call()
    }
}
