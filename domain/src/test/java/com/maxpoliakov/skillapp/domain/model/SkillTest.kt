package com.maxpoliakov.skillapp.domain.model

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import java.time.Duration

class SkillTest : StringSpec({
    "recordedTime" {
        Skill("", Duration.ofHours(1), Duration.ZERO).recordedTime shouldBe Duration.ofHours(1)
        Skill("", Duration.ofHours(2), Duration.ofHours(1)).recordedTime shouldBe Duration.ofHours(1)
    }
})
