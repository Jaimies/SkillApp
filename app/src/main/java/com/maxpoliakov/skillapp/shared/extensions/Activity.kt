package com.maxpoliakov.skillapp.shared.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.maxpoliakov.skillapp.R

fun AppCompatActivity.findNavHostFragment() : NavHostFragment {
    return supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
}
