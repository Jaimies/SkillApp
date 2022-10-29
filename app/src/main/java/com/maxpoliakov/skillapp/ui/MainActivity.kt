package com.maxpoliakov.skillapp.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.maxpoliakov.skillapp.R.id.history_fragment_dest
import com.maxpoliakov.skillapp.R.id.settings_fragment_dest
import com.maxpoliakov.skillapp.R.id.skills_fragment_dest
import com.maxpoliakov.skillapp.R.id.statistics_fragment_dest
import com.maxpoliakov.skillapp.R.style.Theme_SkillApp
import com.maxpoliakov.skillapp.databinding.MainActBinding
import com.maxpoliakov.skillapp.domain.repository.StopwatchUtil
import com.maxpoliakov.skillapp.model.Intro
import com.maxpoliakov.skillapp.ui.intro.FirstRunIntro
import com.maxpoliakov.skillapp.ui.intro.IntroUtil
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.ui.findNavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    lateinit var navController: NavController
    private lateinit var binding: MainActBinding

    @Inject
    lateinit var stopwatchUtil: StopwatchUtil

    @Inject
    lateinit var introUtil: IntroUtil

    private val appBarConfiguration = AppBarConfiguration(
        setOf(
            skills_fragment_dest,
            history_fragment_dest,
            statistics_fragment_dest,
            settings_fragment_dest,
        )
    )

    val toolbar get() = binding.toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(Theme_SkillApp)
        super.onCreate(savedInstanceState)
        introUtil.showIfNecessary(Intro.FirstRunIntro) { FirstRunIntro.show(this) }
        binding = MainActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavController()
    }

    override fun onResume() {
        super.onResume()
        stopwatchUtil.updateState()
        stopwatchUtil.updateNotification()
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
        binding.toolbar.isGone = destination.id == statistics_fragment_dest
    }
}
