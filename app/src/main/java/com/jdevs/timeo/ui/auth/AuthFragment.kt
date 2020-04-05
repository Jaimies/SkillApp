package com.jdevs.timeo.ui.auth

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.jdevs.timeo.R
import com.jdevs.timeo.util.EMPTY
import com.jdevs.timeo.util.INVALID
import com.jdevs.timeo.util.fragment.navigateToGraph
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.hardware.hideKeyboard
import com.jdevs.timeo.util.validateEmail

abstract class AuthFragment : Fragment() {

    abstract val viewModel: AuthViewModel

    protected fun navigateToOverview() = navigateToGraph(R.id.overview)

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.hideKeyboard) { hideKeyboard() }
    }

    protected fun checkEmail(email: String): Boolean {

        emailError = when (validateEmail(email)) {
            EMPTY -> R.string.email_empty
            INVALID -> R.string.email_invalid
            else -> {
                viewModel.emailError.value = ""
                return true
            }
        }

        return false
    }

    var emailError: Int
        get() = -1
        set(@StringRes value) {
            viewModel.emailError.value = getString(value)
        }

    var passwordError: Int
        get() = -1
        set(@StringRes value) {
            viewModel.passwordError.value = getString(value)
        }
}
