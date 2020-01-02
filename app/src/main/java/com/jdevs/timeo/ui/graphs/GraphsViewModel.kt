package com.jdevs.timeo.ui.graphs

import com.jdevs.timeo.common.viewmodel.ListViewModel
import com.jdevs.timeo.data.source.TimeoRepository
import javax.inject.Inject

class GraphsViewModel @Inject constructor(
    private val repository: TimeoRepository
) : ListViewModel() {

    override val liveData get() = repository.stats
}