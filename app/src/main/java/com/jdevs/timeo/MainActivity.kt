package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {


    private lateinit var navController : NavController

    private lateinit var mToggle : ActionBarDrawerToggle

    private var mToolBarNavigationListenerIsRegistered = false

    private val mainDestinations = setOf(R.id.homeFragment, R.id.taskListFragment, R.id.statsFragment)

    private val loginDestinations = setOf(R.id.loginFragment, R.id.signupFragment)

    private val nonBackArrowedDestinations = loginDestinations.union(mainDestinations)


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navController = Navigation.findNavController(this, R.id.nav_host_fragment).apply {

            addOnDestinationChangedListener(this@MainActivity)

            bottomNavView.setupWithNavController(this)

            val appBarConfiguration = AppBarConfiguration(
                nonBackArrowedDestinations,
                drawerLayout
            )


            setSupportActionBar(toolbar)
            supportActionBar?.apply {

                setDisplayHomeAsUpEnabled(false)
                setDefaultDisplayHomeAsUpEnabled(false)
                setHomeButtonEnabled(false)

            }

            mToggle = object : ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            ) {

                override fun onDrawerClosed(drawerView: View) {
                    super.onDrawerClosed(drawerView)
                    invalidateOptionsMenu()
                    syncState()
                }

                override fun onDrawerOpened(drawerView: View) {
                    super.onDrawerOpened(drawerView)
                    invalidateOptionsMenu()
                    syncState()
                }

            }

            mToggle.setToolbarNavigationClickListener {

                Log.i(TAG, "Button was pressed")

                onBackPressed()

            }

            drawerLayout.addDrawerListener(mToggle)

            NavigationUI.setupActionBarWithNavController(
                this@MainActivity,
                this,
                appBarConfiguration
            )

            navView.setupWithNavController(this)

            enableButton(true)

        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return NavigationUI.navigateUp(navController, null)

    }

    private fun enableButton(enable: Boolean) {

        if(!::mToggle.isInitialized) {

            return

        }

        if (enable) {

            mToggle.isDrawerIndicatorEnabled = false

            supportActionBar!!.setDisplayHomeAsUpEnabled(true)


            if (!mToolBarNavigationListenerIsRegistered) {

                mToggle.setToolbarNavigationClickListener {

                    onBackPressed()

                }

                mToolBarNavigationListenerIsRegistered = true

            }

        } else {

            mToggle.isDrawerIndicatorEnabled = true

            supportActionBar!!.setDisplayHomeAsUpEnabled(false)


            mToggle.toolbarNavigationClickListener = null

            mToolBarNavigationListenerIsRegistered = false

        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addActivity -> {

                navController.navigate(R.id.action_showCreateActivityFragment)

            }

            android.R.id.home -> {

                navController.navigateUp()

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

            else -> {

                return super.onOptionsItemSelected(item)

            }
        }

        return true

    }



    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

        val id = destination.id


        when {

            mainDestinations.contains(id) -> {

                bottomNavView.visibility = View.VISIBLE

                enableButton(false)

            }

            loginDestinations.contains(id) -> {



            }

            else -> {

                bottomNavView.visibility = View.GONE

                enableButton(true)

            }
        }

    }

    override fun onBackPressed() {

        if(drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START)

            return

        }

        super.onBackPressed()

    }
}
