package com.jdevs.timeo

import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jdevs.timeo.util.extensions.setupWithNavController
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.lazyUnsynchronized
import kotlinx.android.synthetic.main.main_act.bottom_nav_view
import kotlinx.android.synthetic.main.main_act.nav_host_container
import kotlinx.android.synthetic.main.main_act.toolbar

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private lateinit var currentNavController: LiveData<NavController>
    private val bottomNavView by lazyUnsynchronized { bottom_nav_view }
    private val appBarConfiguration by lazyUnsynchronized { AppBarConfiguration(topLevelDestinations) }
    private val graphsToRecreate = mutableListOf<Int>()

    private val navGraphIds = listOf(
        R.navigation.overview,
        R.navigation.summary,
        R.navigation.stay_focused,
        R.navigation.profile
    )

    private val topLevelDestinations = setOf(
        R.id.overview_fragment_dest,
        R.id.activities_fragment_dest,
        R.id.stay_focused_dest,
        R.id.profile_fragment_dest
    )

    private val knownActions = arrayOf(
        R.id.action_activitiesFragment_to_addActivityFragment,
        R.id.action_activitiesFragment_to_historyFragment,
        R.id.action_profileFragment_to_settingsFragment,
        R.id.action_projectsFragment_to_addProjectFragment
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)

        currentNavController = bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        ).also {

            it.observe(this) { navController ->

                setupActionBarWithNavController(navController)
                navController.addOnDestinationChangedListener(this)
            }
        }
    }

    override fun onSupportNavigateUp() =
        currentNavController.value?.navigateUp(appBarConfiguration) ?: false || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        currentNavController.value?.run {

            if (knownActions.contains(item.itemId)) {

                navigate(item.itemId)
            }
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val graphId = controller.graph.id

        if (graphsToRecreate.contains(graphId)) {

            nav_host_container.post {

                val options = NavOptions.Builder()
                    .setPopUpTo(graphId, true)
                    .build()

                controller.navigate(controller.graph.startDestination, null, options)
            }

            graphsToRecreate.remove(graphId)
        }

        hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (isFinishing) {

            (application as TimeoApplication).onDestroy()
        }
    }

    fun navigateToGraph(@IdRes graphId: Int, graphs: List<Int> = listOf(R.navigation.summary)) {

        bottomNavView.selectedItemId = graphId

        nav_host_container.post {

            currentNavController.value?.popBackStack()
        }

        graphs.forEach {

            if (!graphsToRecreate.contains(it)) graphsToRecreate.add(it)
        }
    }
}
