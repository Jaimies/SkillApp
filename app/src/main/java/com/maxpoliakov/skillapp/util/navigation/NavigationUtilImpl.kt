package com.maxpoliakov.skillapp.util.navigation

import androidx.navigation.NavController
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class NavigationUtilImpl @Inject constructor(
    private val navController: NavController,
) : NavigationUtil {

    override fun navigateToSkillDetail(skillId: Int) {
        val directions = MainDirections.actionToSkillDetailFragment(skillId)
        navController.navigateAnimated(directions)
    }
}
