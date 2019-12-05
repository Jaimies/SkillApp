package com.jdevs.timeo

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.lazyUnsynchronized
import com.jdevs.timeo.util.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.bottom_nav_view
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private var currentNavController: LiveData<NavController>? = null
    private val bottomNavView by lazyUnsynchronized { bottom_nav_view }
    private val appBarConfiguration by lazy { AppBarConfiguration(topLevelDestinations.toSet()) }

    private val topLevelDestinations by lazy {
        listOf(R.id.overviewFragment, R.id.activityListFragment, R.id.profileFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val navGraphIds = listOf(
            R.navigation.overview,
            R.navigation.activity_list,
            R.navigation.profile
        )

        currentNavController = bottomNavView?.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        )?.also {

            it.observe(this) { navController ->
                setupActionBarWithNavController(navController)
                navController.addOnDestinationChangedListener(this)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {

        return currentNavController?.value?.navigateUp(appBarConfiguration) ?: false || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addActivity -> {
                currentNavController?.value?.navigate(R.id.action_showCreateActivityFragment)
            }

            R.id.history -> {
                currentNavController?.value?.navigate(R.id.action_showHistory)
            }
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {

        hideKeyboard(this)
    }

    fun navigateToGraph(graphId: Int) {

        bottomNavView.selectedItemId = graphId

        toolbar.post {

            currentNavController?.value?.popBackStack()
        }
    }
}
