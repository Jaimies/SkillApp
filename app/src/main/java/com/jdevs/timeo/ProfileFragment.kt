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
import com.jdevs.timeo.navigators.ProfileNavigator
import com.jdevs.timeo.viewmodels.ProfileFragmentViewModel

class ProfileFragment : Fragment(), ProfileNavigator {

    private val mAuth = FirebaseAuth.getInstance()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(ProfileFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentProfileBinding.inflate(inflater, container, false)

        binding.viewmodel = viewModel

        viewModel.isUserLoggedIn = !(mAuth.currentUser?.isAnonymous ?: true)
        viewModel.userEmail = mAuth.currentUser?.email.orEmpty()

        viewModel.navigator = this

        // Inflate the layout for this fragment
        return binding.root
    }

    override fun login() {
        findNavController().navigate(R.id.action_login)
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        findNavController().navigate(R.id.action_logout)
    }
}
