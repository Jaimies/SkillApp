package com.maxpoliakov.skillapp.data.config

import com.google.android.gms.tasks.Task
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import java.time.Duration

class RemoteConfigRepositoryImplTest : StringSpec({
    val remoteConfig = mock(FirebaseRemoteConfig::class.java)
    val task = mock(Task::class.java)
    `when`(remoteConfig.fetchAndActivate()).thenReturn(task as Task<Boolean>)
    val repository = RemoteConfigRepositoryImpl(remoteConfig)

    "getAdInterval() converts the number of seconds to duration" {
        `when`(task.isComplete).thenReturn(true)
        `when`(remoteConfig.getLong("ad_interval")).thenReturn(20L)
        repository.getAdInterval() shouldBe Duration.ofSeconds(20)
        `when`(remoteConfig.getLong("ad_interval")).thenReturn(40L)
        repository.getAdInterval() shouldBe Duration.ofSeconds(40)
    }

    "getAdInterval() returns Long.MAX_VALUE seconds if the values from Config haven't been fetched yet" {
        `when`(task.isComplete).thenReturn(false)
        repository.getAdInterval() shouldBe Duration.ofSeconds(Long.MAX_VALUE)
    }
})
