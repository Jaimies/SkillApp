package com.maxpoliakov.skillapp.ui.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.maxpoliakov.skillapp.shared.util.collectOnce
import com.maxpoliakov.skillapp.util.lifecycle.SingleLiveEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class DetailsViewModel : ViewModel() {
    protected abstract val nameFlow: Flow<String>

    private val _isEditing = MutableLiveData(false)
    val isEditing: LiveData<Boolean> get() = _isEditing

    private val _onSave = SingleLiveEvent<Nothing>()
    val onSave: LiveData<Nothing> get() = _onSave

    val name = MutableLiveData("")
    val inputIsValid = name.map { it?.isBlank() == false }

    private var lastName = ""

    init {
        viewModelScope.launch {
            delay(1)
            nameFlow.collectOnce { newName ->
                name.value = newName
                lastName = newName
            }
        }
    }

    fun enterEditingMode() {
        _isEditing.value = true
    }

    fun exitEditingMode() {
        name.value = lastName
        _isEditing.value = false
    }

    abstract suspend fun update(name: String)

    fun save() {
        name.value?.trim()?.takeIf(String::isNotEmpty)?.let { name ->
            viewModelScope.launch { update(name) }
            lastName = name
        }

        _isEditing.value = false
        _onSave.call()
    }
}
