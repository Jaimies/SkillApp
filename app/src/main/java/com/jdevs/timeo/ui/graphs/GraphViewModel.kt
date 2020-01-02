package com.jdevs.timeo.ui.graphs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.data.RecordStats
import com.jdevs.timeo.util.toFriendlyDate
import com.jdevs.timeo.util.toHours

class GraphViewModel : ViewModel() {

    val day get() = _day as LiveData<String>
    val time get() = _time as LiveData<String>

    private val _day = MutableLiveData("")
    private val _time = MutableLiveData("")

    fun setStatsInfo(recordStats: RecordStats) {

        _time.value = recordStats.time.toHours() + "h"
        _day.value = recordStats.day.toFriendlyDate()
    }
}
