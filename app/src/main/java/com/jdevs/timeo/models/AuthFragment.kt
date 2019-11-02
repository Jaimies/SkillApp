package com.jdevs.timeo.models

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.R
import kotlinx.android.synthetic.main.activity_main.*

open class AuthFragment : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()

    val mUser = mAuth.currentUser


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        if (mUser == null) {

            returnToLoginFragment()

            return

        }

    }

    private fun returnToLoginFragment() {

        findNavController().apply {

            navigate(R.id.action_login)

        }

        requireActivity().toolbar.apply {

            navigationIcon = null

        }

    }

}

