package com.theskillapp.skillapp.screenshots

import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.annotation.StringRes
import com.theskillapp.skillapp.domain.model.Skill
import com.theskillapp.skillapp.domain.model.Record
import com.theskillapp.skillapp.domain.model.Goal
import com.theskillapp.skillapp.domain.model.MeasurementUnit
import com.theskillapp.skillapp.domain.model.Id
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.records.DBRecord
import com.theskillapp.skillapp.data.serialization.DBMeasurementUnit.Companion.mapToUI
import java.time.Duration
import java.time.LocalDate
import com.theskillapp.skillapp.test.R

val skill_pullUps = createSkill(id = 1, R.string.pull_ups, 2500, -1, MeasurementUnit.Times)
val skill_jogging = createSkill(id = 2, R.string.jogging, 350_000, -1, MeasurementUnit.Meters)
val skill_webDesign = createSkill(id = 3, R.string.web_design, Duration.ofHours(479).toMillis(), 1, MeasurementUnit.Millis)
val skill_appDesign = createSkill(id = 4, R.string.app_design, Duration.ofHours(677).toMillis(), 1, MeasurementUnit.Millis, Duration.ofHours(3).toMillis())

val skills = listOf(
    skill_pullUps,
    skill_jogging,
    skill_webDesign,
    skill_appDesign,
)

val records = listOf(
    createRecord(skill_webDesign, 0, Duration.ofHours(1)),
    createRecord(skill_appDesign, 0, Duration.ofMinutes(90)),
    createRecord(skill_webDesign, 0, Duration.ofHours(1)),
    createRecord(skill_webDesign, 0, Duration.ofHours(1)),
    createRecord(skill_appDesign, 0, Duration.ofHours(2)),

    createRecord(skill_appDesign, 1, Duration.ofHours(1)),
    createRecord(skill_webDesign, 1, Duration.ofMinutes(90)),
    createRecord(skill_appDesign, 1, Duration.ofHours(2)),
    createRecord(skill_webDesign, 1, Duration.ofHours(2)),

    createRecord(skill_webDesign, 2, Duration.ofHours(3)),
    createRecord(skill_appDesign, 2, Duration.ofHours(3)),

    createRecord(skill_webDesign, 3, Duration.ofHours(3)),
    createRecord(skill_appDesign, 3, Duration.ofHours(4)),

    createRecord(skill_webDesign, 4, Duration.ofHours(3)),
    createRecord(skill_appDesign, 4, Duration.ofMinutes(150)),

    createRecord(skill_webDesign, 5, Duration.ofHours(3)),
    createRecord(skill_appDesign, 5, Duration.ofHours(4)),

    createRecord(skill_webDesign, 6, Duration.ofHours(3)),
    createRecord(skill_appDesign, 6, Duration.ofHours(3)),
)

private fun createRecord(
    skill: DBSkill,
    daysAgoCreated: Int,
    duration: Duration
) = Record(
    skillId = skill.id,
    name = skill.name,
    date = LocalDate.now().minusDays(daysAgoCreated.toLong()),
    count = duration.toMillis(),
    unit = MeasurementUnit.Millis,
)

private fun createSkill(
    id: Id,
    @StringRes nameResId: Int,
    totalCount: Long,
    groupId: Int,
    unit: MeasurementUnit<*>,
    goalCount: Long = 0,
): DBSkill {
    val name: String = getInstrumentation().context.getString(nameResId)

    return DBSkill(
        id = id,
        name = name,
        totalTime = totalCount,
        groupId = groupId,
        unit = unit.mapToUI(),
        goalType = Goal.Type.Daily,
        goalTime = goalCount,
    )
}

