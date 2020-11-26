package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.annotation.MenuRes
import androidx.fragment.app.Fragment

abstract class ActionBarFragment(@MenuRes private val menuId: Int) : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(menuId, menu)
    }
}
