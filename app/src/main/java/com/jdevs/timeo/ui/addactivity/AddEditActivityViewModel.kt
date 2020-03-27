package com.jdevs.timeo.ui.addactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.model.Activity
import com.jdevs.timeo.domain.usecase.activities.AddActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.DeleteActivityUseCase
import com.jdevs.timeo.domain.usecase.activities.GetActivitiesFromCacheUseCase
import com.jdevs.timeo.domain.usecase.activities.SaveActivityUseCase
import com.jdevs.timeo.lifecycle.SingleLiveEvent
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.model.mapToDomain
import com.jdevs.timeo.shared.util.mapList
import com.jdevs.timeo.ui.common.viewmodel.KeyboardHidingViewModel
import com.jdevs.timeo.util.lifecycle.launchCoroutine
import javax.inject.Inject
import kotlin.LazyThreadSafetyMode.NONE

class AddEditActivityViewModel @Inject constructor(
    getActivitiesFromCache: GetActivitiesFromCacheUseCase,
    private val addActivity: AddActivityUseCase,
    private val saveActivity: SaveActivityUseCase,
    private val deleteActivity: DeleteActivityUseCase
) : KeyboardHidingViewModel() {

    val name = MutableLiveData<String>()
    val parentActivity = MutableLiveData<String>()
    val showDeleteDialog = SingleLiveEvent<Any>()
    val nameError = MutableLiveData<String>()
    val parentActivityError = MutableLiveData<String>()
    val activities by lazy(NONE) { getActivitiesFromCache(activityId) }
    val activityNames by lazy(NONE) { activities.mapList(Activity::name) }
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

    fun addActivity(name: String) = launchCoroutine { addActivity.invoke(name) }
    suspend fun saveActivity(activity: ActivityItem) = saveActivity.invoke(activity.mapToDomain())

    fun deleteActivity(activity: ActivityItem) = launchCoroutine {

        deleteActivity(activity.mapToDomain())
    }

    fun showDeleteDialog() = showDeleteDialog.call()
}
