package com.maxpoliakov.skillapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.parcelize.Parcelize
import java.time.Duration
import java.time.LocalDate

@Keep
@Parcelize
data class SkillItem(
    override val id: Int,
    val name: String,
    val totalTime: Duration,
    val initialTime: Duration,
    val lastWeekTime: Duration,
    val creationDate: LocalDate,
    val order: Int,
) : ViewItem, Parcelable

fun Skill.mapToPresentation() = SkillItem(
    id, name, totalTime, initialTime, lastWeekTime, date, order,
)

fun SkillItem.mapToDomain() = Skill(
    name,
    totalTime,
    initialTime,
    lastWeekTime,
    id,
    creationDate,
    order,
)
