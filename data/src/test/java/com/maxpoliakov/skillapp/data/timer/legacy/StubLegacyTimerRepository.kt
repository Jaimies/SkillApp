package com.maxpoliakov.skillapp.data.timer.legacy

import com.maxpoliakov.skillapp.domain.model.Timer
import com.maxpoliakov.skillapp.domain.repository.LegacyTimerRepository

class StubLegacyTimerRepository(timer: Timer?) : LegacyTimerRepository {
    private var _timer = timer

    override fun getTimer(): Timer? = _timer

    override fun deleteTimer() {
        _timer = null
    }
}
