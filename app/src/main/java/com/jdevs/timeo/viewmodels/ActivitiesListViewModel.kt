package com.jdevs.timeo.viewmodels

import androidx.lifecycle.ViewModel
import com.jdevs.timeo.livedata.ActivitiesListLiveData

class ActivitiesListViewModel : ViewModel() {
    private val activitiesListRepository = FirestoreActivitiesListRepository()

    fun getActivitiesListLiveData(): ActivitiesListLiveData? {
        return activitiesListRepository.getActivitiesListLiveData()
    }

    interface ActivitiesListRepository {
        fun getActivitiesListLiveData(): ActivitiesListLiveData?
    }
}