package com.jdevs.timeo.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.observe
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.ui.setupActionBarWithNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.shared.util.lazyUnsafe
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.navigation.setupWithNavController
import kotlinx.android.synthetic.main.main_act.bottom_nav_view
import kotlinx.android.synthetic.main.main_act.nav_host_container
import kotlinx.android.synthetic.main.main_act.toolbar

class MainActivity : AppCompatActivity(),
    NavController.OnDestinationChangedListener {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var currentNavController: LiveData<NavController>
    private val bottomNavView by lazyUnsafe { bottom_nav_view }
    private var graphToRecreate = -1

    private val navGraphIds =
        listOf(R.navigation.overview, R.navigation.calendar, R.navigation.profile)

    private val knownActions = arrayOf(
        R.id.addactivity_fragment_dest,
        R.id.addproject_fragment_dest,
        R.id.history_fragment_dest,
        R.id.settings_fragment_dest
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_Timeo)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.main_act)
        setSupportActionBar(toolbar)

        currentNavController = bottomNavView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_container,
            intent = intent
        ).also {

            it.observe(this) { navController ->

                setupActionBarWithNavController(navController)
                navController.addOnDestinationChangedListener(this)
            }
        }

        viewModel.run {

            if (selectedItemId != -1 && selectedItemId != bottomNavView.selectedItemId) {

                bottomNavView.selectedItemId = selectedItemId
                bottomNavView.jumpDrawablesToCurrentState()
            }
        }
    }

    override fun onSupportNavigateUp() =
        currentNavController.value?.navigateUp() ?: false || super.onSupportNavigateUp()

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId in knownActions) {

            currentNavController.value?.navigateAnimated(item.itemId)
            return true
        }

        return false
    }

    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        val graphId = controller.graph.id

        if (graphId == graphToRecreate) {

            nav_host_container.post {

                val options = NavOptions.Builder()
                    .setPopUpTo(graphId, true)
                    .build()

                controller.navigate(controller.graph.startDestination, null, options)
            }

            graphToRecreate = -1
        }

        window.decorView.hideKeyboard()
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModel.selectedItemId = bottomNavView.selectedItemId

        if (isFinishing) {
            (application as TimeoApplication).onDestroy()
        }
    }

    fun navigateToGraph(@IdRes graphId: Int) {

        graphToRecreate = graphId
        bottomNavView.selectedItemId = graphId
    }

    class MainViewModel : ViewModel() {

        var selectedItemId = -1
    }
}
