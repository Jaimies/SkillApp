package com.jdevs.timeo.ui.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.domain.usecase.tasks.AddTaskUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import javax.inject.Inject

class AddTaskViewModel(
    private val addTask: AddTaskUseCase,
    private val projectId: String
) : ViewModel() {

    val name = MutableLiveData<String>()
    val dismiss = SingleLiveEvent<Any>()

    fun addTask() = launchCoroutine {
        if (name.value?.isNotEmpty() == true) {
            addTask.invoke(name.value!!, projectId)
        }

        dismiss()
    }

    fun dismiss() = dismiss.call()

    class Factory @Inject constructor(private val addTask: AddTaskUseCase) {
        fun create(projectId: String) = AddTaskViewModel(addTask, projectId)
    }
}
