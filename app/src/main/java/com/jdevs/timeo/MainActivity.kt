package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.jdevs.timeo.util.hideKeyboard
import com.jdevs.timeo.util.lazyUnsynchronized
import kotlinx.android.synthetic.main.activity_main.bottom_nav_view
import kotlinx.android.synthetic.main.activity_main.drawer_layout
import kotlinx.android.synthetic.main.activity_main.nav_view

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val bottomNavView by lazyUnsynchronized { bottom_nav_view ?: null }
    private val navView by lazyUnsynchronized { nav_view ?: null }
    private val drawerLayout by lazyUnsynchronized { drawer_layout ?: null }

    private val mainDestinations by lazy {
        setOf(R.id.overviewFragment, R.id.activitiesListFragment, R.id.statsFragment)
    }

    private val topLevelDestinations by lazy {
        mainDestinations + setOf(R.id.profileFragment, R.id.settingsFragment)
    }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(topLevelDestinations, drawerLayout)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView?.setupWithNavController(navController)
        bottomNavView?.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {

        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addActivity -> {
                navController.navigate(R.id.action_showCreateActivityFragment)
            }

            R.id.history -> {
                navController.navigate(R.id.action_showHistory)
            }

            R.id.achievements -> {
                navController.navigate(R.id.action_showAchievements)
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
}
