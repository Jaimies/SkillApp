package com.maxpoliakov.skillapp.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.domain.model.Activity
import com.maxpoliakov.skillapp.domain.model.toMinimal
import com.maxpoliakov.skillapp.domain.usecase.activities.AddActivityUseCase
import com.maxpoliakov.skillapp.domain.usecase.activities.DeleteActivityUseCase
import com.maxpoliakov.skillapp.domain.usecase.activities.GetParentActivitySuggestionsUseCase
import com.maxpoliakov.skillapp.domain.usecase.activities.SaveActivityUseCase
import com.maxpoliakov.skillapp.lifecycle.SingleLiveEvent
import com.maxpoliakov.skillapp.model.ActivityItem
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.viewmodel.KeyboardHidingViewModel
import com.maxpoliakov.skillapp.util.lifecycle.mapList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.time.Duration
import javax.inject.Inject

private const val NAME_MAX_LENGTH = 100

class AddEditActivityViewModel(
    getParentActivitySuggestions: GetParentActivitySuggestionsUseCase,
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase,
    private val ioScope: CoroutineScope,
    private val activity: ActivityItem?
) : KeyboardHidingViewModel() {

    val name = MutableLiveData(activity?.name)
    val totalTime = MutableLiveData<String>()
    val nameError = MutableLiveData(-1)
    val parentActivityError = MutableLiveData(-1)
    val showDeleteDialog = SingleLiveEvent<Any>()
    val parentActivityName = activity?.parentActivity?.name
    val activityExists = activity != null

    val navigateBack: LiveData<Any> get() = _navigateBack
    private val _navigateBack = SingleLiveEvent<Any>()

    var parentActivityIndex = -1

    val activities =
        getParentActivitySuggestions.run(activity?.id ?: -1).asLiveData()
    val activityNames = activities.mapList(Activity::name)

    fun deleteActivity() = ioScope.launch {
        activity?.let {
            deleteActivity.run(activity.mapToDomain())
        }
    }

    // Since the first item in dropdown is "–" and other activities start at index of 1,
    // we need to subtract 1 from index when trying to get the needed item
    private fun getParentActivity(index: Int) =
        activities.value!!.getOrNull(index - 1)

    fun saveActivity() {

        val name = name.value.orEmpty()
        if (!validateName(name)) return

        val index = parentActivityIndex

        if (index == -1) {
            parentActivityError.value = R.string.invalid_activity_error
            return
        }

        val parentActivity = getParentActivity(index)

        ioScope.launch {

            if (activity != null) {

                val newActivity = activity.mapToDomain().run {
                    if (index == -1) copy(name = name)
                    else copy(
                        name = name,
                        parentActivity = parentActivity?.toMinimal()
                    )
                }

                saveActivity.run(newActivity)
            } else {
                addActivity.run(
                    Activity(
                        name = name,
                        totalTime = Duration.ofHours(totalTime.value?.toLong()?: 0),
                        parentActivity = parentActivity?.toMinimal()
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
        private val getParentActivitySuggestions: GetParentActivitySuggestionsUseCase,
        private val addActivity: AddActivityUseCase,
        private val saveActivity: SaveActivityUseCase,
        private val deleteActivity: DeleteActivityUseCase,
        private val ioScope: CoroutineScope
    ) {
        fun create(activity: ActivityItem?) = AddEditActivityViewModel(
            getParentActivitySuggestions, addActivity,
            saveActivity, deleteActivity, ioScope, activity
        )
    }
}
