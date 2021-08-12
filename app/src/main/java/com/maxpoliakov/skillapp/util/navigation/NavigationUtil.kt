package com.maxpoliakov.skillapp.util.navigation

interface NavigationUtil {
    fun navigateToSkillDetail(skillId: Int)
    fun navigateToSkillGroupDetail(groupId: Int)
    fun navigateUp()
}
