package com.maxpoliakov.skillapp.data.backup

import com.maxpoliakov.skillapp.data.group.DBGroup
import com.maxpoliakov.skillapp.data.records.DBRecord
import com.maxpoliakov.skillapp.data.skill.DBSkill
import com.maxpoliakov.skillapp.data.stats.DBStatistic
import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val skills: List<DBSkill>,
    val records: List<DBRecord>,
    val stats: List<DBStatistic>,
    val groups: List<DBGroup>,
)
