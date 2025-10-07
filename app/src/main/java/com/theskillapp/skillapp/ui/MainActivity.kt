package com.theskillapp.skillapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.theskillapp.skillapp.databinding.MainActBinding
import com.theskillapp.skillapp.domain.stopwatch.Stopwatch
import com.theskillapp.skillapp.ui.intro.FirstRunIntro
import com.theskillapp.skillapp.ui.intro.IntroUtil
import com.theskillapp.skillapp.shared.hardware.hideKeyboard
import com.theskillapp.skillapp.shared.extensions.findNavHostFragment
import com.theskillapp.skillapp.shared.navigation.switchedTabs
import com.theskillapp.skillapp.shared.navigation.topLevelDestinationIds
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    lateinit var navController: NavController
    private lateinit var binding: MainActBinding

    @Inject
    lateinit var stopwatch: Stopwatch

    @Inject
    lateinit var introUtil: IntroUtil

    private val appBarConfiguration = AppBarConfiguration(topLevelDestinationIds)

    private var previousDestination: NavDestination? = null

    val toolbar get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!introUtil.hasFirstRunIntroBeenShown()) {
            FirstRunIntro.show(this)
            introUtil.markFirstRunIntroAsShown();
        }

        binding = MainActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavController()
    }

    private fun setupNavController() {
        navController = findNavHostFragment().navController
        binding.bottomAppBar.setupWithNavController(navController)
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

        navController.currentBackStackEntry
            ?.savedStateHandle
            ?.set(SWITCHED_BOTTOM_NAV_VIEW_TABS, switchedTabs(previousDestination, destination))

        previousDestination = destination
    }

    companion object {
        const val SWITCHED_BOTTOM_NAV_VIEW_TABS = "SWITCHED_BOTTOM_NAV_VIEW_TABS"
    }
}
