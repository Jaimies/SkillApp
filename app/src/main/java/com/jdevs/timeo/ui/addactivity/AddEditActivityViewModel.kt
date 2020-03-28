package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.usecase.activities.AddActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.DeleteActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesFromCacheUseCase
import com.jdevs.timeo.domain.usecase.activities.SaveActivityUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class AddEditActivityViewModel @Inject constructor(
    getActivitiesFromCache: GetActivitiesFromCacheUseCase,
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData<String>()

    val parentActivityName get() = _parentActivityName as LiveData<String>
    private val _parentActivityName = MutableLiveData<String>()
    val nameError = MutableLiveData<String>()
    val parentActivityError = MutableLiveData<String>()

    val activityExists get() = _activityExists as LiveData<Boolean>
    private val _activityExists = MutableLiveData(false)
    val showDeleteDialog = SingleLiveEvent<Any>()
    var parentActivityIndex: Int? = PARENT_ACTIVITY_UNCHANGED

    val activities by lazy(NONE) { getActivitiesFromCache(activityId) }
    val activityNames by lazy(NONE) {
        activities.map {
            it.map(Activity::name).apply { this as MutableList; add(0, "–") }
        }
    }

    private var activityId = ""

    fun setActivity(activity: ActivityItem) {

        if (name.value != null) {
            return
        }

        name.value = activity.name
        activityId = activity.id
        _parentActivityName.value = activity.parentActivityName
        _activityExists.value = true
    }

    suspend fun saveActivity(oldActivity: ActivityItem, parentActivityIndex: Int) {

        if (parentActivityIndex == PARENT_ACTIVITY_UNCHANGED) {
            saveActivity(oldActivity.copy(name = name.value!!).mapToDomain())
            return
        }

        val parentActivity = getParentActivity(parentActivityIndex)

        saveActivity.invoke(
            oldActivity.copy(
                name = name.value!!, parentActivityName = parentActivity?.name.orEmpty(),
                parentActivityId = parentActivity?.id.orEmpty()
            ).mapToDomain()
        )
    }

    fun addActivity(parentActivityIndex: Int) = launchCoroutine {

        val parentActivity = getParentActivity(parentActivityIndex)

        addActivity(
            Activity(
                "", name.value!!, 0, 0, OffsetDateTime.now(),
                parentActivity?.name.orEmpty(), parentActivity?.id.orEmpty()
            )
        )
    }

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {

        deleteActivity(activity.mapToDomain())
    }

    private fun getParentActivity(index: Int) =
        if (index >= 1) activities.value!![index - 1] else null

    fun showDeleteDialog() = showDeleteDialog.call()

    companion object {
        private const val PARENT_ACTIVITY_UNCHANGED = -2
    }
}
