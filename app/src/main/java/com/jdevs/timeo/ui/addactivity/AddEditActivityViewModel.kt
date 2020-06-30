package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.LiveData
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

class AddEditActivityViewModel @Inject constructor(
    getParentActivitySuggestions: GetParentActivitySuggestionsUseCase,
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData<String>()
    val totalTime = MutableLiveData<String>()
    val nameError = MutableLiveData<String>()
    val parentActivityError = MutableLiveData<String>()
    val showDeleteDialog = SingleLiveEvent<Any>()

    val parentActivityName get() = _parentActivityName as LiveData<String>
    private val _parentActivityName = MutableLiveData<String>()
    val activityExists get() = _activityExists as LiveData<Boolean>
    private val _activityExists = MutableLiveData(false)

    var parentActivityIndex: Int? = PARENT_ACTIVITY_UNCHANGED
    private var activityId = ""

    val activities by lazy(NONE) { getParentActivitySuggestions(activityId) }
    val activityNames by lazy(NONE) { activities.mapList(Activity::name) }

    fun setActivity(activity: ActivityItem) {

        if (name.value != null) {
            return
        }

        activityId = activity.id
        name.value = activity.name
        _parentActivityName.value = activity.parentActivity?.name
        _activityExists.value = true
    }

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

    companion object {
        private const val PARENT_ACTIVITY_UNCHANGED = -2
    }
}
