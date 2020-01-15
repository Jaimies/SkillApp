package com.jdevs.timeo.data

import androidx.lifecycle.MutableLiveData
import com.jdevs.timeo.domain.repository.SettingsRepository
import javax.inject.Inject

class FakeSettingsRepository @Inject constructor() :
    SettingsRepository {

    override val activitiesEnabled get() = _activitiesEnabled
    private val _activitiesEnabled = MutableLiveData(false)

    override fun setActivitiesEnabled(isEnabled: Boolean) = _activitiesEnabled.postValue(isEnabled)
}
