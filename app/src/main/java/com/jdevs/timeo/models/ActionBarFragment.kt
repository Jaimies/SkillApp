package com.jdevs.timeo.models

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment

open class ActionBarFragment : Fragment() {
    protected open val menuId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        if (menuId != -1) {
            inflater.inflate(menuId, menu)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}
