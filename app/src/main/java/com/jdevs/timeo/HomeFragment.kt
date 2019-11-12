package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.utilities.TAG
import kotlinx.android.synthetic.main.partial_circular_loader.view.spinningProgressBar

class HomeFragment : ActionBarFragment() {

    private lateinit var mLoader: FrameLayout

    private val mAuth = FirebaseAuth.getInstance()

    private var mUser = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = mUser

        if (user == null || user.providerId == "") {

            signInAnonymously()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.apply {

            mLoader = spinningProgressBar
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
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
