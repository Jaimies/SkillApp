package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.model.toMinimal
import com.jdevs.timeo.domain.usecase.activities.AddActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.DeleteActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.GetParentActivitySuggestionsUseCase
import com.jdevs.timeo.domain.usecase.activities.SaveActivityUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import com.jdevs.timeo.util.time.getMins
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class AddEditActivityViewModel(
    getParentActivitySuggestions: GetParentActivitySuggestionsUseCase,
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase,
    private val activity: ActivityItem?
) : KeyboardHidingViewModel() {

    val name = MutableLiveData<String>(activity?.name)
    val totalTime = MutableLiveData<String>()
    val nameError = MutableLiveData<String>()
    val parentActivityError = MutableLiveData<String>()
    val showDeleteDialog = SingleLiveEvent<Any>()
    val parentActivityName = activity?.parentActivity?.name
    val activityExists = activity != null

    var parentActivityIndex: Int? = PARENT_ACTIVITY_UNCHANGED

    val activities by lazy(NONE) { getParentActivitySuggestions(activity?.id.orEmpty()) }
    val activityNames by lazy(NONE) { activities.mapList(Activity::name) }

    suspend fun saveActivity(oldActivity: ActivityItem, parentActivityIndex: Int) {

        if (parentActivityIndex == PARENT_ACTIVITY_UNCHANGED) {
            saveActivity(oldActivity.copy(name = name.value!!).mapToDomain())
            return
        }

        val parentActivity = getParentActivity(parentActivityIndex)

        saveActivity.invoke(
            oldActivity.mapToDomain()
                .copy(name = name.value!!, parentActivity = parentActivity?.toMinimal())
        )
    }

    fun addActivity(parentActivityIndex: Int) = launchCoroutine {

        val parentActivity = getParentActivity(parentActivityIndex)
        addActivity(
            Activity(
                "", name.value!!, getMins(totalTime.value?.toInt() ?: 0, 0),
                0, OffsetDateTime.now(), parentActivity?.toMinimal()
            )
        )
    }

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {
        deleteActivity(activity.mapToDomain())
    }

    // Since the first item in dropdown is "–" and other activities start at index of 1,
    // we need to subtract 1 from index when trying to get the needed item
    private fun getParentActivity(index: Int) = activities.value!!.getOrNull(index - 1)

    fun showDeleteDialog() = showDeleteDialog.call()

    class Factory @Inject constructor(
        private val getParentActivitySuggestions: GetParentActivitySuggestionsUseCase,
        private val addActivity: AddActivityUseCase,
        private val saveActivity: SaveActivityUseCase,
        private val deleteActivity: DeleteActivityUseCase
    ) {
        fun create(activity: ActivityItem?) = AddEditActivityViewModel(
            getParentActivitySuggestions, addActivity, saveActivity, deleteActivity, activity
        )
    }

    companion object {
        private const val PARENT_ACTIVITY_UNCHANGED = -2
    }
}
