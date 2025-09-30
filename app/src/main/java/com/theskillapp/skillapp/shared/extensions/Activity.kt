package com.theskillapp.skillapp.shared.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.theskillapp.skillapp.R

fun AppCompatActivity.findNavHostFragment() : NavHostFragment {
    return supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
}
