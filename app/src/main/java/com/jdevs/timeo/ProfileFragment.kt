package com.jdevs.timeo


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.models.AuthFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : AuthFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)


        view.logoutButton.setOnClickListener {

            FirebaseAuth.getInstance().signOut()

            findNavController().navigate(R.id.action_logout)

            requireActivity().toolbar.navigationIcon = null

        }

        // Inflate the layout for this fragment
        return view
    }


}
