package com.jdevs.timeo.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.model.TaskItem

class TaskViewModel : ViewModel() {

    val name: LiveData<String> get() = _name
    val isCompleted: LiveData<Boolean> get() = _isCompleted
    private val _name = MutableLiveData("")
    private val _isCompleted = MutableLiveData<Boolean>()

    fun setTask(task: TaskItem) {

        _name.value = task.name
        _isCompleted.value = task.isCompleted
    }

    fun setCompleted(isCompleted: Boolean) {

        _isCompleted.value = isCompleted
    }
}
