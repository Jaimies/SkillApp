package com.jdevs.timeo.ui.graphs

import androidx.lifecycle.LiveData
import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.TimeoRepository
import com.jdevs.timeo.util.GraphTypes.DAY
import com.jdevs.timeo.util.GraphTypes.WEEK
import javax.inject.Inject

class GraphsViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData: LiveData<*>?
        get() = when (graphType) {

            DAY -> repository.dayStats
            WEEK -> repository.weekStats
            else -> repository.monthStats
        }
    private var graphType = DAY

    fun setGraphType(type: Int) {

        graphType = type
    }
}
