package com.jdevs.timeo.models

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.jdevs.timeo.R

open class AuthFragment : Fragment() {

    lateinit var auth : FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null) {

            throw FirebaseAuthException("Anauthorized access", "User does not have permissions to use this fragment")

        }

    }

    private fun returnToLoginFragment() {

        findNavController().navigate(R.id.loginFragment)

    }

}

