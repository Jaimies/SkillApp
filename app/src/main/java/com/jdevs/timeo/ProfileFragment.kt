package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.databinding.FragmentProfileBinding
import com.jdevs.timeo.viewmodels.ProfileViewModel

class ProfileFragment : Fragment(), ProfileViewModel.Navigator {

    private val auth = FirebaseAuth.getInstance()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProfileViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel.apply {

            isUserLoggedIn = !(auth.currentUser?.isAnonymous ?: true)
            userEmail = auth.currentUser?.email.orEmpty()

            navigator = this@ProfileFragment
        }

        return binding.root
    }

    override fun login() {
        findNavController().navigate(R.id.action_login)
    }

    override fun logout() {
        viewModel.logout()
        findNavController().navigate(R.id.action_logout)
    }
}
