package com.jdevs.timeo.models

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater

open class ActionBarFragment : AuthFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)

    }

    fun addOptionsMenu(menu: Menu, inflater : MenuInflater, id : Int) {

        inflater.inflate(id, menu)

        super.onCreateOptionsMenu(menu, inflater)

    }

}