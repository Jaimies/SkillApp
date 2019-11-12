package com.jdevs.timeo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.view.loginButton
import kotlinx.android.synthetic.main.fragment_profile.view.loginLayout
import kotlinx.android.synthetic.main.fragment_profile.view.logoutButton
import kotlinx.android.synthetic.main.fragment_profile.view.logoutLayout
import kotlinx.android.synthetic.main.fragment_profile.view.userEmailEditText

class ProfileFragment : Fragment() {

    private val mAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        if (mAuth.currentUser?.isAnonymous == true) {

            view.loginLayout.visibility = View.VISIBLE

            view.loginButton.setOnClickListener {

                findNavController().navigate(R.id.action_login)
            }
        } else {

            view.logoutButton.setOnClickListener {

                FirebaseAuth.getInstance().signOut()

                findNavController().navigate(R.id.action_logout)
            }

            view.logoutLayout.visibility = View.VISIBLE

            view.userEmailEditText.text = "Your email: ${mAuth.currentUser?.email}"
        }

        // Inflate the layout for this fragment
        return view
    }
}
