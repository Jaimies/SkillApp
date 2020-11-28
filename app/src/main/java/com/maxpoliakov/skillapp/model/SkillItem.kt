package com.maxpoliakov.skillapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.Skill
import kotlinx.android.parcel.Parcelize
import java.time.Duration
import java.time.LocalDateTime

@Keep
@Parcelize
data class SkillItem(
    override val id: Int,
    override val name: String,
    override val totalTime: Duration,
    val lastWeekTime: Duration,
    val creationDate: LocalDateTime
) : Recordable, Parcelable

fun Skill.mapToPresentation() = SkillItem(
    id, name, totalTime, lastWeekTime, timestamp
)

fun SkillItem.mapToDomain() = Skill(
    name,
    totalTime,
    lastWeekTime,
    id,
    creationDate
)
