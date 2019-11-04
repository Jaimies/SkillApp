package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.MenuInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class HomeFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mCreateNewActivityView: LinearLayout
    private lateinit var mCreateNewActivityButton: MaterialButton

    private val mAuth = FirebaseAuth.getInstance()

    private var mUser = mAuth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mCreateNewActivityView = view.createNewActivityView

        mCreateNewActivityButton = view.createNewActivityButton

        mLoader = view.listLoader as FrameLayout

        mRecyclerView = view.activitiesRecyclerView

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
            mRecyclerView,
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

                mActivitiesRef = mFirestore
                    .collection("users/${mUser!!.uid}/activities")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(20)

                setupActivityListener(
                    mRecyclerView,
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
