package com.jdevs.timeo.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.ui.setupActionBarWithNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.navigation.setupWithNavController
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_act.bottom_nav_view
import kotlinx.android.synthetic.main.main_act.nav_host_container

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var currentNavController: LiveData<NavController>
    private var graphToRecreate = -1

    private val navGraphIds =
        intArrayOf(R.navigation.overview, R.navigation.calendar, R.navigation.profile)

    private val knownActions = intArrayOf(
        R.id.addactivity_fragment_dest,
        R.id.addproject_fragment_dest,
        R.id.history_fragment_dest,
        R.id.settings_fragment_dest
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)

        currentNavController = bottom_nav_view.setupWithNavController(
            navGraphIds, supportFragmentManager,
            R.id.nav_host_container, intent
        )

        currentNavController.observe(this) { navController ->
            setupActionBarWithNavController(navController)
            navController.addOnDestinationChangedListener(this)
        }

        val selectedItemId = savedInstanceState?.getInt(SELECTED_ITEM_ID) ?: -1

        if (selectedItemId != bottom_nav_view.selectedItemId) {
            bottom_nav_view.selectedItemId = selectedItemId
            bottom_nav_view.jumpDrawablesToCurrentState()
        }
    }

    override fun onSupportNavigateUp() =
        currentNavController.value?.navigateUp() ?: false || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId in knownActions) {
            currentNavController.value?.navigateAnimated(item.itemId)
            return true
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        if (controller.graph.id == graphToRecreate) {

            nav_host_container.post {
                val options = NavOptions.Builder()
                    .setPopUpTo(controller.graph.id, true)
                    .build()

                controller.navigate(controller.graph.startDestination, null, options)
            }

            graphToRecreate = -1
        }

        window.decorView.hideKeyboard()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SELECTED_ITEM_ID, bottom_nav_view.selectedItemId)
        super.onSaveInstanceState(outState)
    }

    fun navigateToGraph(@IdRes graphId: Int) {
        graphToRecreate = graphId
        bottom_nav_view.selectedItemId = graphId
    }

    companion object {
        private const val SELECTED_ITEM_ID = "selectedItemId"
    }
}
