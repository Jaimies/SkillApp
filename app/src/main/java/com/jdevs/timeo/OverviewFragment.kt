package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.util.TAG
import kotlinx.android.synthetic.main.preloader.view.preloader

class OverviewFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private var user = auth.currentUser

    private lateinit var preloader: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_overview, container, false)
        preloader = view.preloader

        if (user == null || user?.providerId == "") {

            signInAnonymously()
        }

        return view
    }

    private fun signInAnonymously() {

        auth.signInAnonymously()
            .addOnCompleteListener {
                preloader.visibility = View.GONE
            }
            .addOnSuccessListener { result ->

                user = result.user
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Failed to sign in anonymously", exception)
            }

        preloader.visibility = View.VISIBLE
    }
}
