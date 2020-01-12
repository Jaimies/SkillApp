package com.jdevs.timeo.ui.profile

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ProfileFragBinding
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.navigateToGraph
import com.jdevs.timeo.util.extensions.observeEvent
import javax.inject.Inject

class ProfileFragment : ActionBarFragment() {

    override val menuId = R.menu.profile_fragment_menu

    @Inject
    lateinit var viewModel: ProfileViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ProfileFragBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel.apply {

            observeEvent(navigateToSignIn) {

                findNavController().navigate(R.id.signin_fragment_dest)
            }

            observeEvent(signOut) {

                signOut()
                navigateToGraph(R.id.overview)
            }
        }

        return binding.root
    }
}
