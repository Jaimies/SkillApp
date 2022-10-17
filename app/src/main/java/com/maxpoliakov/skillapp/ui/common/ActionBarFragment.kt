package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle.State.RESUMED

abstract class ActionBarFragment(@MenuRes private val menuId: Int) : BaseFragment() {
    protected lateinit var menu: Menu

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuId, menu)
            this@ActionBarFragment.menu = menu
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return onMenuItemSelected(menuItem.itemId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, RESUMED)
    }

    abstract fun onMenuItemSelected(id: Int): Boolean
}
