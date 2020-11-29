package com.maxpoliakov.skillapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import androidx.navigation.NavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
import kotlinx.android.synthetic.main.bottom_sheet_frag.navigation_view

class BottomSheetFragment(
    private val navController: NavController
) : BottomSheetDialogFragment(),
    NavigationView.OnNavigationItemSelectedListener {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_frag, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        navigation_view.setNavigationItemSelectedListener(this)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        navigateToDestination(item)
        return true
    }

    private fun navigateToDestination(item: MenuItem) {
        if (!isAtDestination(item)) {
            navController.navigateAnimated(item.itemId)
        }

        hide()
    }

    private fun isAtDestination(item: MenuItem): Boolean {
        return navController.currentDestination?.id == item.itemId
    }

    private fun hide() {
        parentFragmentManager.commit {
            remove(this@BottomSheetFragment)
        }
    }
}
