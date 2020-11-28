package com.maxpoliakov.skillapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupActionBarWithNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.R.id.addskill_fragment_dest
import com.maxpoliakov.skillapp.R.id.history_fragment_dest
import com.maxpoliakov.skillapp.R.navigation.overview
import com.maxpoliakov.skillapp.R.navigation.profile
import com.maxpoliakov.skillapp.R.style.Theme_Timeo
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.navigation.setupWithNavController
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_act.bottom_nav_view

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var navController: LiveData<NavController>

    private val navGraphIds = intArrayOf(overview, profile)

    private val navActions = intArrayOf(
        addskill_fragment_dest,
        history_fragment_dest
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Theme_Timeo)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setupNavController()
        savedInstanceState?.let(::restoreBottomNavState)
    }

    private fun restoreBottomNavState(savedInstanceState: Bundle) {
        val selectedItemId = savedInstanceState.getInt(SELECTED_ITEM_ID)

        if (selectedItemId != bottom_nav_view.selectedItemId) {
            bottom_nav_view.selectedItemId = selectedItemId
            bottom_nav_view.jumpDrawablesToCurrentState()
        }
    }

    private fun setupNavController() {
        navController = bottom_nav_view.setupWithNavController(
            navGraphIds, supportFragmentManager,
            R.id.nav_host_container, intent
        )

        setupNavControllerObserver()
    }

    private fun setupNavControllerObserver() {
        navController.observe(this) { navController ->
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener(this)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navigateUp() || super.onSupportNavigateUp()
    }

    private fun navigateUp(): Boolean {
        return navController.value?.navigateUp() ?: false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId in navActions) {
            navController.value?.navigateAnimated(item.itemId)
            return true
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        hideKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM_ID, bottom_nav_view.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val SELECTED_ITEM_ID = "selectedItemId"
    }
}
