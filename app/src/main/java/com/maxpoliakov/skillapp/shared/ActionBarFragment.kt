package com.maxpoliakov.skillapp.shared

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Lifecycle.State.RESUMED

abstract class ActionBarFragment<T: ViewDataBinding>(@MenuRes private val menuId: Int) : DataBindingFragment<T>() {
    protected var menu: Menu? = null

    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            if (menuId != -1) menuInflater.inflate(menuId, menu)
            this@ActionBarFragment.menu = menu
            onMenuCreated(menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return onMenuItemSelected(menuItem.itemId)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().addMenuProvider(menuProvider, viewLifecycleOwner, RESUMED)
    }

    open fun onMenuItemSelected(id: Int): Boolean {
        return false
    }

    protected open fun onMenuCreated(menu: Menu) {}
}
