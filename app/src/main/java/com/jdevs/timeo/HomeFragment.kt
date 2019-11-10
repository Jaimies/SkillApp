package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.jdevs.timeo.models.ActivitiesListFragment
import com.jdevs.timeo.utilities.TAG
import kotlinx.android.synthetic.main.partial_activities_list.view.activitiesRecyclerView
import kotlinx.android.synthetic.main.partial_activities_list.view.createNewActivityButton
import kotlinx.android.synthetic.main.partial_activities_list.view.createNewActivityView
import kotlinx.android.synthetic.main.partial_activities_list.view.listLoader

class HomeFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout

    private val mAuth = FirebaseAuth.getInstance()

    private var mUser = mAuth.currentUser

    private lateinit var mActivitiesRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.apply {

            mLoader = listLoader as FrameLayout

            mActivitiesRecyclerView = activitiesRecyclerView

            mCreateNewActivityView = createNewActivityView
            mCreateNewActivityButton = createNewActivityButton
        }

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()

        val user = mUser

        if (user == null || user.providerId == "") {

            signInAnonymously()

            return
        }

        setupActivityListener(
            mActivitiesRecyclerView,
            mLoader,
            mCreateNewActivityView,
            mCreateNewActivityButton
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        addOptionsMenu(menu, inflater, R.menu.action_bar_main)
    }

    private fun signInAnonymously() {

        mAuth.signInAnonymously()
            .addOnSuccessListener { result ->

                mUser = result.user

                mLoader.visibility = View.GONE

                setupActivityListener(
                    mActivitiesRecyclerView,
                    mLoader,
                    mCreateNewActivityView,
                    mCreateNewActivityButton
                )
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Failed to sign in anonymously", exception)
            }

        mLoader.visibility = View.VISIBLE
    }
}
