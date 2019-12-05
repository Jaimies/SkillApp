package com.jdevs.timeo.common

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import androidx.fragment.app.Fragment

abstract class ActionBarFragment : Fragment() {
    protected abstract val menuId: Int

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(menuId, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
