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
import kotlinx.android.synthetic.main.partial_circular_loader.view.spinningProgressBar

class OverviewFragment : Fragment() {

    private lateinit var mLoader: FrameLayout

    private val mAuth = FirebaseAuth.getInstance()

    private var mUser = mAuth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_overview, container, false)

        view.apply {

            mLoader = spinningProgressBar
        }

        val user = mUser

        if (user == null || user.providerId == "") {

            signInAnonymously()
        }

        // Inflate the layout for this fragment
        return view
    }

    private fun signInAnonymously() {

        mAuth.signInAnonymously()
            .addOnCompleteListener {
                mLoader.visibility = View.GONE
            }
            .addOnSuccessListener { result ->

                mUser = result.user
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Failed to sign in anonymously", exception)
            }

        mLoader.visibility = View.VISIBLE
    }
}
