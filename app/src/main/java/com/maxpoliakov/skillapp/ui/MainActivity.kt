package com.maxpoliakov.skillapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.R.id.history_fragment_dest
import com.maxpoliakov.skillapp.R.id.settings_fragment_dest
import com.maxpoliakov.skillapp.R.id.skills_fragment_dest
import com.maxpoliakov.skillapp.R.id.statistics_fragment_dest
import com.maxpoliakov.skillapp.R.style.Theme_SkillApp
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.ui.findNavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_act.bottom_app_bar
import kotlinx.android.synthetic.main.main_act.toolbar

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private lateinit var navController: NavController

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            skills_fragment_dest,
            history_fragment_dest,
            statistics_fragment_dest,
            settings_fragment_dest
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Theme_SkillApp)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)
        setupNavController()
    }

    private fun setupNavController() {
        navController = findNavHostFragment().navController
        bottom_app_bar.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        hideKeyboard()
        toolbar.isGone = destination.id == statistics_fragment_dest
    }

    fun setToolbar(newToolbar: Toolbar) {
        toolbar.isGone = newToolbar.id != R.id.toolbar
        setSupportActionBar(newToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    fun resetToolbar() {
        setToolbar(toolbar)
    }
}
