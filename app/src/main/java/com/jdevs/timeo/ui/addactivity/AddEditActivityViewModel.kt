package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.usecase.activities.AddActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.DeleteActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.SaveActivityUseCase
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.launchCoroutine
import com.jdevs.timeo.util.livedata.SingleLiveEvent
import javax.inject.Inject

class AddEditActivityViewModel @Inject constructor(
    private val addActivity: AddActivityUseCase,
    private val saveActivityUseCase: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData("")
    val nameError get() = _nameError as LiveData<String>
    val activityExists get() = _activityExists as LiveData<Boolean>
    val showDeleteDialog = SingleLiveEvent<Any>()
    val saveActivity = SingleLiveEvent<String>()

    private val _nameError = MutableLiveData("")
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: ActivityItem?) {

        name.value = activity?.name.orEmpty()
        _activityExists.value = true
    }

    fun setNameError(error: String) {

        _nameError.value = error
    }

    fun triggerSaveActivity() {

        saveActivity.value = name.value.orEmpty()
    }

    fun addActivity(name: String) = launchCoroutine {

        addActivity.invoke(name)
    }

    fun saveActivity(activity: ActivityItem) = launchCoroutine {

        saveActivityUseCase.invoke(activity.mapToDomain())
    }

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {

        deleteActivity.invoke(activity.mapToDomain())
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
