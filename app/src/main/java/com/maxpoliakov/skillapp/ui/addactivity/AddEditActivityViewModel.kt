package com.maxpoliakov.skillapp.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.domain.usecase.activities.AddActivityUseCase
import com.maxpoliakov.skillapp.domain.usecase.activities.DeleteActivityUseCase
import com.maxpoliakov.skillapp.domain.usecase.activities.SaveActivityUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.viewmodel.KeyboardHidingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

private const val NAME_MAX_LENGTH = 100

class AddEditActivityViewModel(
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase,
    private val ioScope: CoroutineScope,
    private val activity: ActivityItem?
) : KeyboardHidingViewModel() {

    val name = MutableLiveData(activity?.name)
    val totalTime = MutableLiveData<String>()
    val nameError = MutableLiveData(-1)
    val showDeleteDialog = SingleLiveEvent<Any>()
    val activityExists = activity != null

    val navigateBack: LiveData<Any> get() = _navigateBack
    private val _navigateBack = SingleLiveEvent<Any>()

    fun deleteActivity() = ioScope.launch {
        activity?.let {
            deleteActivity.run(activity.mapToDomain())
        }
    }

    fun saveActivity() {

        val name = name.value.orEmpty()
        if (!validateName(name)) return

        ioScope.launch {

            if (activity != null) {
                val newActivity = activity.mapToDomain().copy(name = name)

                saveActivity.run(newActivity)
            } else {
                addActivity.run(
                    Activity(
                        name = name,
                        totalTime = Duration.ofHours(totalTime.value?.toLong() ?: 0)
                    )
                )
            }
        }

        _navigateBack.call()
    }

    private fun validateName(name: String): Boolean {
        when {
            name.isEmpty() -> nameError.value = R.string.name_empty
            name.length >= NAME_MAX_LENGTH -> nameError.value =
                R.string.name_too_long
            else -> return true
        }

        return false
    }

    fun showDeleteDialog() = showDeleteDialog.call()

    class Factory @Inject constructor(
        private val addActivity: AddActivityUseCase,
        private val saveActivity: SaveActivityUseCase,
        private val deleteActivity: DeleteActivityUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(activity: ActivityItem?) = AddEditActivityViewModel(
            addActivity, saveActivity,
            deleteActivity, ioScope, activity
        )
    }
}
