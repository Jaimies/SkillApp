package com.jdevs.timeo.ui.tasks

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.lifecycle.SmartLiveData
import com.jdevs.timeo.model.TaskItem

class TaskViewModel {

    val name: LiveData<String> get() = _name
    val isCompleted = SmartLiveData<Boolean>()
    private val _name = MutableLiveData<String>()

    fun setTask(task: TaskItem) {

        _name.value = task.name
        isCompleted.value = task.isCompleted
    }
}
