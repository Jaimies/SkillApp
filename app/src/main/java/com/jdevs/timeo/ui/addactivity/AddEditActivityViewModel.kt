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
    val parentActivityIndex = MutableLiveData(-1)
    val showDeleteDialog = SingleLiveEvent<Any>()
    val nameError = MutableLiveData<String>()
    val parentActivityError = MutableLiveData<String>()
    val activities by lazy(NONE) { getActivitiesFromCache(activityId) }
    val activityNames by lazy(NONE) {
        activities.map {
            it.map(Activity::name).apply { this as MutableList; add(0, "–") }
        }
    }
    val activityExists get() = _activityExists as LiveData<Boolean>
    private var activityId = ""
    private val _activityExists = MutableLiveData(false)

    fun setActivity(activity: ActivityItem) {

        if (name.value != null) {
            return
        }

        name.value = activity.name
        activityId = activity.id
        _activityExists.value = true
    }

    fun setParentActivityIndex(index: Int?) {
        parentActivityIndex.value = if (index != null) index - 1 else index
    }

    suspend fun saveActivity(activity: ActivityItem) = saveActivity.invoke(activity.mapToDomain())

    fun addActivity(name: String, parentActivityId: String) = launchCoroutine {

        addActivity.invoke(Activity("", name, 0, 0, OffsetDateTime.now(), parentActivityId))
    }

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {

        deleteActivity(activity.mapToDomain())
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
