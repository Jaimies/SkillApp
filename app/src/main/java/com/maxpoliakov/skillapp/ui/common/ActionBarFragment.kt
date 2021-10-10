package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes

abstract class ActionBarFragment(@MenuRes private val menuId: Int) : BaseFragment() {
    protected lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        this.menu = menu
        inflater.inflate(menuId, menu)
    }
}
