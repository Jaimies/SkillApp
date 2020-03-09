package com.jdevs.timeo.ui.signin

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.fragment.navigateToGraph

abstract class AuthFragment : Fragment() {

    abstract val viewModel: AuthViewModel

    protected fun navigateToOverview() = navigateToGraph(R.id.overview)

    var emailError: Int
        get() = -1
        set(@StringRes value) = viewModel.setEmailError(getString(value))

    var passwordError: Int
        get() = -1
        set(@StringRes value) = viewModel.setPasswordError(getString(value))
}
