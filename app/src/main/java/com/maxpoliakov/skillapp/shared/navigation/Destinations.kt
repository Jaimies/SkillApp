package com.maxpoliakov.skillapp.shared.navigation

import com.maxpoliakov.skillapp.R

val destinationIdsGroupedByTab = setOf(
    setOf(
        R.id.skills_fragment_dest,
        R.id.addskill_fragment_dest,
        R.id.skill_detail_fragment_dest,
        R.id.skill_group_fragment_dest,
    ),

    setOf(
        R.id.history_fragment_dest,
    ),

    setOf(
        R.id.statistics_fragment_dest,
    ),

    setOf(
        R.id.settings_fragment_dest,
        R.id.backup_fragment_dest,
        R.id.restore_backup_fragment_dest,
    ),
)

val topLevelDestinationIds = setOf(
    R.id.skills_fragment_dest,
    R.id.history_fragment_dest,
    R.id.statistics_fragment_dest,
    R.id.settings_fragment_dest,
)
