package com.jdevs.timeo.viewmodels

import android.view.View
import androidx.databinding.BindingAdapter
import androidx.lifecycle.ViewModel
import com.jdevs.timeo.navigators.ProfileNavigator

class ProfileViewModel : ViewModel() {
    @JvmField
    var isUserLoggedIn = false
    @JvmField
    var userEmail = ""

    var navigator: ProfileNavigator? = null
}

@BindingAdapter("hideIf")
fun hideIf(view: View, value: Boolean) {
    view.visibility = if (value) View.GONE else View.VISIBLE
}
