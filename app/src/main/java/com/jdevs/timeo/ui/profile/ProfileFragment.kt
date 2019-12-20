package com.jdevs.timeo.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ProfileFragBinding
import com.jdevs.timeo.ui.profile.viewmodel.ProfileViewModel
import com.jdevs.timeo.util.navigateToGraph
import com.jdevs.timeo.util.observeEvent

class ProfileFragment : ActionBarFragment() {

    override val menuId = R.menu.profile_fragment_menu
    private val auth = FirebaseAuth.getInstance()
    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = ProfileFragBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel.apply {

            if (auth.currentUser?.isAnonymous == false) {

                notifyUserIsSignedIn()
            }

            observeEvent(navigateToSignIn) {

                findNavController().navigate(R.id.action_profileFragment_to_signInFragment)
            }

            observeEvent(signOut) {

                signOut()
                navigateToGraph(R.id.overview)
            }
        }

        return binding.root
    }
}
