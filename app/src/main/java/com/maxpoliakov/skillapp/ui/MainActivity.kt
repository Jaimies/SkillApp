package com.maxpoliakov.skillapp.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
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
import com.maxpoliakov.skillapp.ui.intro.TheIntro
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
    lateinit var sharedPreferences: SharedPreferences

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
        showIntroIfNeeded()
        binding = MainActBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        setupNavController()
        sharedPreferences.edit { putBoolean("intro_viewed", false) }
    }

    private fun showIntroIfNeeded() {
        val introViewed = sharedPreferences.getBoolean("intro_viewed", false)
        if (introViewed) return

        val intent = Intent(this, TheIntro::class.java)
        startActivity(intent)

        sharedPreferences.edit { putBoolean("intro_viewed", true) }
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
