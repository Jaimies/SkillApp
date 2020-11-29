package com.maxpoliakov.skillapp.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.maxpoliakov.skillapp.BottomSheetFragment
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.R.id.addskill_fragment_dest
import com.maxpoliakov.skillapp.R.id.history_fragment_dest
import com.maxpoliakov.skillapp.R.id.profile_fragment_dest
import com.maxpoliakov.skillapp.R.id.skills_fragment_dest
import com.maxpoliakov.skillapp.R.style.Theme_Timeo
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.ui.findNavHostFragment
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_act.bottom_app_bar

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController

    private val navActions = intArrayOf(
        addskill_fragment_dest,
        history_fragment_dest
    )

    private val appBarConfiguration = AppBarConfiguration(
        setOf(skills_fragment_dest, history_fragment_dest, profile_fragment_dest)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Theme_Timeo)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setupNavController()
        setupBottomAppBar()
    }

    private fun setupNavController() {
        navController = findNavHostFragment().navController
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(this)
    }

    private fun setupBottomAppBar() {
        bottom_app_bar.setNavigationOnClickListener {
            val fragment = BottomSheetFragment(navController)
            fragment.show(supportFragmentManager, null)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId in navActions) {
            navController.navigateAnimated(item.itemId)
            return true
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        hideKeyboard()
    }
}
