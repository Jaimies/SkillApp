package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
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
import kotlinx.android.synthetic.main.activity_main.drawerLayout
import kotlinx.android.synthetic.main.activity_main.nav_view
import kotlinx.android.synthetic.main.activity_main.toolbar

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val mainDestinations by lazy {
        setOf(R.id.overviewFragment, R.id.activitiesListFragment, R.id.statsFragment)
    }

    private val topLevelDestinations by lazy {
        mainDestinations.union(setOf(R.id.profileFragment, R.id.settingsFragment))
    }

    private val appBarConfiguration by lazy {
        AppBarConfiguration(topLevelDestinations, drawerLayout)
    }

    private val mToggle by lazy {
        ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
    }

    private val navController by lazy { findNavController(R.id.nav_host_fragment) }
    private val bottomNavView by lazyUnsynchronized { bottom_nav_view }
    private val navView by lazyUnsynchronized { nav_view }

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        drawerLayout.addDrawerListener(mToggle)

        navController.addOnDestinationChangedListener(this)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        bottomNavView.setupWithNavController(navController)
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

        bottomNavView.visibility =
            if (shouldDisplayBottomNav(destination)) View.VISIBLE else View.GONE

        toolbar.post {
            mToggle.syncState()
        }
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)

            return
        }

        super.onBackPressed()
    }

    private fun shouldDisplayBottomNav(destination: NavDestination): Boolean {
        val id = destination.id

        val res = obtainStyledAttributes(
            R.style.Widget_Timeo_BottomNavigationView,
            intArrayOf(android.R.attr.visibility)
        )

        val result = res.getString(0)

        res.recycle()

        return mainDestinations.contains(id) && result == "0"
    }
}
