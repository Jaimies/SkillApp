package com.jdevs.timeo.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.SigninFragBinding
import com.jdevs.timeo.util.fragment.observe
import dagger.hilt.android.AndroidEntryPoint

private const val RC_SIGN_IN = 0

@AndroidEntryPoint
class SignInFragment : AuthFragment() {

    override val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = SigninFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.showGoogleSignInIntent) { startActivityForResult(it, RC_SIGN_IN) }
        observe(viewModel.navigateToSignUp) {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {

        if (requestCode == RC_SIGN_IN) {
            viewModel.onSignInCompleted(intent)
        }
    }
}
