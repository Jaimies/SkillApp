package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.usecase.activities.AddActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.DeleteActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.SaveActivityUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import javax.inject.Inject

class AddEditActivityViewModel @Inject constructor(
    private val addActivity: AddActivityUseCase,
    private val saveActivityUseCase: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData<String>()
    val showDeleteDialog = SingleLiveEvent<Any>()
    val nameError get() = _nameError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>
    private val _nameError = MutableLiveData<String>()
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: ActivityItem) {

        if (name.value != null) {
            return
        }

        name.value = activity.name
        _activityExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun addActivity(name: String) = launchCoroutine {

        addActivity.invoke(name)
    }

    suspend fun saveActivity(activity: ActivityItem) {

        saveActivityUseCase.invoke(activity.mapToDomain())
    }

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {

        deleteActivity.invoke(activity.mapToDomain())
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
