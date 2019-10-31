package com.jdevs.timeo.models

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.jdevs.timeo.R

open class AuthFragment : Fragment() {

    lateinit var mAuth : FirebaseAuth

    lateinit var mUser : FirebaseUser


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        mAuth = FirebaseAuth.getInstance()

        mUser = mAuth.currentUser
            ?: throw FirebaseAuthException("Unauthorized access", "User does not have permissions to use this fragment")

    }

    private fun returnToLoginFragment() {

        findNavController().navigate(R.id.loginFragment)

    }

}

