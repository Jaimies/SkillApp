package com.jdevs.timeo

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.jdevs.timeo.models.ActivitiesListFragment
import kotlinx.android.synthetic.main.partial_activities_list.view.*

class HomeFragment : ActivitiesListFragment() {

    private lateinit var mLoader: FrameLayout

    private val mAuth = FirebaseAuth.getInstance()

    private var mUser = mAuth.currentUser

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        mLoader = view.listLoader as FrameLayout

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
            view!!.activitiesRecyclerView,
            mLoader,
            view!!.createNewActivityView,
            view!!.createNewActivityButton
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
                    view!!.activitiesRecyclerView,
                    mLoader,
                    view!!.createNewActivityView,
                    view!!.createNewActivityButton
                )
            }
            .addOnFailureListener { exception ->

                Log.w(TAG, "Failed to sign in anonymously", exception)
            }

        mLoader.visibility = View.VISIBLE
    }
}
