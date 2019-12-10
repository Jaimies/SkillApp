package com.jdevs.timeo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ProfileFragBinding
import com.jdevs.timeo.ui.profile.viewmodel.ProfileViewModel
import com.jdevs.timeo.util.navigateToGraph

class ProfileFragment : Fragment(), ProfileViewModel.Navigator {

    private val auth = FirebaseAuth.getInstance()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java).also {
            it.navigator = this
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ProfileFragBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel.apply {

            if (auth.currentUser?.isAnonymous == false) {

                signIn(auth.currentUser?.email ?: "")
            }
        }

        return binding.root
    }

    override fun signIn() {

        findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
    }

    override fun signOut() {

        viewModel.signOut()
        navigateToGraph(R.id.overview)
    }
}
