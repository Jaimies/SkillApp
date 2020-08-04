package com.jdevs.timeo.ui.auth

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R.id.overview
import com.jdevs.timeo.util.fragment.navigateToGraph
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.snackbar
import com.jdevs.timeo.util.hardware.hideKeyboard

abstract class AuthFragment : Fragment() {
    abstract val viewModel: AuthViewModel

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.snackbar, ::snackbar)
        observe(viewModel.hideKeyboard) { hideKeyboard() }
        observe(viewModel.navigateToOverview) { navigateToGraph(overview) }
    }
}
