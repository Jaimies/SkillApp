package com.theskillapp.skillapp.data.timer.legacy

import com.theskillapp.skillapp.domain.model.Timer
import com.theskillapp.skillapp.domain.repository.LegacyTimerRepository

class StubLegacyTimerRepository(timer: Timer?) : LegacyTimerRepository {
    private var _timer = timer

    override fun getTimer(): Timer? = _timer

    override fun deleteTimer() {
        _timer = null
    }
}
