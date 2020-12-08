package com.maxpoliakov.skillapp.data.config

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.maxpoliakov.skillapp.domain.repository.RemoteConfigRepository
import java.time.Duration
import javax.inject.Inject

class RemoteConfigRepositoryImpl @Inject constructor(
    private val config: FirebaseRemoteConfig
) : RemoteConfigRepository {

    private val setupTask = config.fetchAndActivate()

    override fun getAdInterval(): Duration {
        if (setupTask.isComplete)
            return Duration.ofSeconds(config.getLong("ad_interval"))

        return Duration.ofSeconds(Long.MAX_VALUE)
    }
}
