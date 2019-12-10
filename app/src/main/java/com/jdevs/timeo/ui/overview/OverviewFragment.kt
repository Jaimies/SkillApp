package com.jdevs.timeo.ui.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.databinding.OverviewFragBinding
import com.jdevs.timeo.ui.overview.viewmodel.OverviewViewModel

class OverviewFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = OverviewFragBinding.inflate(inflater, container, false).also {
            it.viewmodel = viewModel
            it.lifecycleOwner = this
        }

        if (auth.currentUser == null || auth.currentUser?.providerId == "") {

            viewModel.signInAnonymously("Failed to sign in anonymously")
        }

        return binding.root
    }
}
