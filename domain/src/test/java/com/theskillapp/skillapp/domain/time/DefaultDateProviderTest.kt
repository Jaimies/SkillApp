package com.theskillapp.skillapp.domain.time

import com.theskillapp.skillapp.domain.repository.StubUserPreferenceRepository
import io.kotest.core.spec.style.StringSpec
import io.kotest.extensions.time.MutableClock
import io.kotest.matchers.shouldBe
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit

class DefaultDateProviderTest : StringSpec({
    "getCurrentDateWithRespectToDayStartTime()" {
        val clock = MutableClock(Instant.EPOCH, ZoneOffset.UTC)
        val preferenceRepository = StubUserPreferenceRepository(dayStartTime = LocalTime.MIDNIGHT)
        val dateProvider = DefaultDateProvider(preferenceRepository, clock)

        dateProvider.getCurrentDateWithRespectToDayStartTime() shouldBe LocalDate.EPOCH

        preferenceRepository.dayStartTime = LocalTime.of(1, 0)
        dateProvider.getCurrentDateWithRespectToDayStartTime() shouldBe LocalDate.EPOCH.minusDays(1)

        clock.withInstant(Instant.EPOCH.plus(1, ChronoUnit.HOURS))
        dateProvider.getCurrentDateWithRespectToDayStartTime() shouldBe LocalDate.EPOCH

        clock.withInstant(Instant.EPOCH.plus(2, ChronoUnit.HOURS))
        dateProvider.getCurrentDateWithRespectToDayStartTime() shouldBe LocalDate.EPOCH
    }
})
