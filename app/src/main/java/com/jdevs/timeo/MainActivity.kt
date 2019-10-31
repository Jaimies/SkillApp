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
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {


    private lateinit var navController : NavController


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navController = Navigation.findNavController(this, R.id.nav_host_fragment).apply {

            addOnDestinationChangedListener(this@MainActivity)

            bottomNavView.setupWithNavController(this)


            val mainDestinations = setOf(
                R.id.homeFragment,
                R.id.taskListFragment,
                R.id.statsFragment,
                R.id.loginFragment,
                R.id.signupFragment
            )

            val appBarConfiguration = AppBarConfiguration(mainDestinations, drawerLayout)


            setSupportActionBar(topActionBar)

            val toggle = ActionBarDrawerToggle(
                this@MainActivity,
                drawerLayout,
                topActionBar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
            )

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            toggle.syncState()


            NavigationUI.setupActionBarWithNavController(
                this@MainActivity,
                this,
                appBarConfiguration
            )

            navView.setupWithNavController(this)

        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return NavigationUI.navigateUp(navController, null)

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

            else -> {

                return super.onOptionsItemSelected(item)

            }
        }

        return true

    }


    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {

        val id = destination.id

        val mainFragmentIds = arrayOf(

            R.id.homeFragment,
            R.id.taskListFragment,
            R.id.statsFragment

        )



        if(!mainFragmentIds.contains(id)) {

            bottomNavView.visibility = View.GONE

        } else {

            bottomNavView.visibility = View.VISIBLE

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
