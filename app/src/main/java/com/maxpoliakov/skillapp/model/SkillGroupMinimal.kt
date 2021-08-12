package com.maxpoliakov.skillapp.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.maxpoliakov.skillapp.domain.model.SkillGroup
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class SkillGroupMinimal(val id: Int, val name: String) : Parcelable

fun SkillGroup.toMinimal() = SkillGroupMinimal(id, name)
