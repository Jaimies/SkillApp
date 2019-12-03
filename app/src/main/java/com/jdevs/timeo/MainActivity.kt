package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
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
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val bottomNavView by lazyUnsynchronized { bottom_nav_view ?: null }
    private var currentNavController: LiveData<NavController>? = null
    private val navView by lazyUnsynchronized { nav_view ?: null }
    private val drawerLayout by lazyUnsynchronized { drawer_layout ?: null }

    private val topLevelDestinations by lazy {
        listOf(
            R.id.overviewFragment,
            R.id.activitiesListFragment,
            R.id.statsFragment,
            R.id.profileFragment,
            R.id.settingsFragment
        )
    }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(topLevelDestinations.toSet(), drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val navGraphIds = listOf(
            R.navigation.overview,
            R.navigation.activity_list,
            R.navigation.stats,
            R.navigation.profile,
            R.navigation.settings
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

            R.id.achievements -> {
                currentNavController?.value?.navigate(R.id.action_showAchievements)
            }

            R.id.shareAchievements -> {

                val sendIntent = Intent().apply {

                    action = Intent.ACTION_SEND

                    putExtra(Intent.EXTRA_TEXT, "Those are my achievements. What do you think?")

                    type = "text/plain"
                }

                val shareIntent = Intent.createChooser(sendIntent, null)

                startActivity(shareIntent)
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

    override fun onBackPressed() {

        if (drawerLayout?.isDrawerOpen(GravityCompat.START) == true) {

            drawerLayout?.closeDrawer(GravityCompat.START)
            return
        }

        super.onBackPressed()
    }

    fun navigateToGraph(graphId : Int) {
        bottomNavView?.selectedItemId = graphId
    }
}
