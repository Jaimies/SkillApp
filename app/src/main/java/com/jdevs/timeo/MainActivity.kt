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
import com.jdevs.timeo.helpers.KeyboardHelper
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val mainDestinations by lazy {
        setOf(R.id.homeFragment, R.id.taskListFragment, R.id.statsFragment)
    }

    private val allTopLevelDestinations by lazy {
        mainDestinations.union(setOf(R.id.profileFragment, R.id.settingsFragment))
    }

    private lateinit var navController: NavController

    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mToggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        navController = findNavController(R.id.nav_host_fragment)

        navController.addOnDestinationChangedListener(this)

        appBarConfiguration = AppBarConfiguration(
            allTopLevelDestinations,
            drawerLayout
        )

        mToggle = object : ActionBarDrawerToggle(
            this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        ) {

            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                syncState()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                syncState()
            }
        }

        drawerLayout.addDrawerListener(mToggle)
        mToggle.syncState()

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

            R.id.historyFragment -> {

                navController.navigate(R.id.action_showHistory)
            }

            R.id.showAchievements -> {

                navController.navigate(R.id.showAchievements)
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

        KeyboardHelper.hideKeyboard(this)

        val id = destination.id

        if (mainDestinations.contains(id)) {

            bottomNavView.visibility = View.VISIBLE
        } else {

            bottomNavView.visibility = View.GONE
        }
    }

    override fun onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START)

            return
        }

        super.onBackPressed()
    }
}
