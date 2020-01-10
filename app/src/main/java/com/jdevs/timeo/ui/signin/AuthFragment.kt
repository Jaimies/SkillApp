package com.jdevs.timeo.ui.signin

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.extensions.navigateToGraph

abstract class AuthFragment : Fragment() {

    abstract val viewModel: AuthViewModel

    protected fun navigateToOverview() {

        viewModel.hideContent()
        navigateToGraph(R.id.overview)
    }

    protected fun setEmailError(@StringRes resId: Int) = viewModel.setEmailError(getString(resId))
    protected fun setPasswordError(@StringRes resId: Int) =
        viewModel.setPasswordError(getString(resId))
}
