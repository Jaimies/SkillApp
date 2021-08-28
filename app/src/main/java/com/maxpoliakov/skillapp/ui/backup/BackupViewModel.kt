package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.usecase.backup.CreateBackupUseCase
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val createBackupUseCase: CreateBackupUseCase,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEvent<Nothing>()
    val signIn: LiveData<Nothing> get() = _signIn

    private val _goToRestore = SingleLiveEvent<Nothing>()
    val goToRestore: LiveData<Nothing> get() = _goToRestore

    private val _backupCreating = MutableLiveData(false)
    val backupCreating: LiveData<Boolean> get() = _backupCreating

    private val _showBackupCreationSucceeded = SingleLiveEvent<Nothing>()
    val showBackupCreationSucceeded: LiveData<Nothing> = _showBackupCreationSucceeded

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

    fun createBackup() = viewModelScope.launch {
        _backupCreating.value = true
        createBackupUseCase.createBackup()
        _backupCreating.value = false
        _showBackupCreationSucceeded.call()
    }

    fun goToRestore() = _goToRestore.call()
}
