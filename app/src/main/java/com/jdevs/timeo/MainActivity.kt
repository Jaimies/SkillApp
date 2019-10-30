package com.jdevs.timeo

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

            bottomNavigationView.setupWithNavController(this)


            val appBarConfiguration = AppBarConfiguration
                .Builder(

                    R.id.homeFragment,
                    R.id.taskListFragment,
                    R.id.statsFragment,
                    R.id.loginFragment,
                    R.id.signupFragment

                ).build()


            setSupportActionBar(topActionBar)


            NavigationUI.setupActionBarWithNavController(
                this@MainActivity,
                this,
                appBarConfiguration
            )

        }


    }

    override fun onSupportNavigateUp(): Boolean {

        return NavigationUI.navigateUp(navController, null)

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.addActivity -> {

                navController.navigate(R.id.createActivityFragment)

            }

            R.id.showHistory -> {

                navController.navigate(R.id.showHistoryFragment)

            }

            R.id.showAchievements -> {

                navController.navigate(R.id.viewAchievements)

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

            bottomNavigationView.visibility = View.GONE

        } else {

            bottomNavigationView.visibility = View.VISIBLE

        }

    }
}
