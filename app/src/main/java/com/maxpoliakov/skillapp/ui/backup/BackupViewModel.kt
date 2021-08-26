package com.maxpoliakov.skillapp.ui.backup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.domain.model.User
import com.maxpoliakov.skillapp.domain.repository.AuthRepository
import com.maxpoliakov.skillapp.domain.repository.BackupRepository
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class BackupViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val backupRepository: BackupRepository,
) : ViewModel() {
    private val _currentUser = MutableLiveData(authRepository.currentUser)
    val currentUser: LiveData<User?> get() = _currentUser

    private val _signIn = SingleLiveEvent<Nothing>()
    val signIn: LiveData<Nothing> get() = _signIn

    private val _backupNames = MutableLiveData<String>()
    val backupData: LiveData<String> get() = _backupNames

    init {
        if (currentUser.value!! != null)
            getBackups()
    }

    private fun getBackups() {
        viewModelScope.launch {
            val names = backupRepository.getBackups()
            _backupNames.value = names.joinToString(", ")
        }
    }

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
        backupRepository.createBackup(LocalDateTime.now().toString())
    }
}
