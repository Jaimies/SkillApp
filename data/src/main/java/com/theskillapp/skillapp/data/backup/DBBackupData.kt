package com.theskillapp.skillapp.data.backup

import com.theskillapp.skillapp.data.group.DBGroup
import com.theskillapp.skillapp.data.records.DBRecord
import com.theskillapp.skillapp.data.skill.DBSkill
import com.theskillapp.skillapp.data.stats.DBStatistic
import kotlinx.serialization.Serializable

@Serializable
data class DBBackupData(
    val skills: List<DBSkill>,
    val records: List<DBRecord>,
    val stats: List<DBStatistic>,
    val groups: List<DBGroup>,
)
