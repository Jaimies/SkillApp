package com.jdevs.timeo.model

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.LoginActivity

open class AuthFragment : Fragment() {

    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()

        if(auth.currentUser == null) {

            returnToLoginActivity()

        }

    }

    private fun returnToLoginActivity() {

        val intent = Intent(activity, LoginActivity::class.java)

        startActivity(intent)

        activity?.finish()

    }

}

