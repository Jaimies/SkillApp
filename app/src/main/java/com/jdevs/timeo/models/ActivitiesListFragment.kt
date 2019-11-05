package com.jdevs.timeo.models

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.jdevs.timeo.R
import com.jdevs.timeo.TAG
import com.jdevs.timeo.data.TimeoActivity

open class ActivitiesListFragment : ActionBarFragment() {

    val mFirestore = FirebaseFirestore.getInstance()
    private val mActivities = ArrayList<TimeoActivity>()
    private val mItemIds = ArrayList<String>()

    lateinit var mActivitiesRef: Query

    private lateinit var mViewAdapter: ActivitiesListAdapter

    private lateinit var mSnapshotListener: ListenerRegistration

    private val mUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mUser == null) {

            return
        }

        mActivitiesRef = mFirestore
            .collection("users/${mUser.uid}/activities")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)
    }

    fun setupActivityListener(
        recyclerView: RecyclerView,
        loaderLayout: FrameLayout,
        createNewActivityView: LinearLayout,
        createNewActivityButton: MaterialButton
    ) {

        loaderLayout.apply {

            visibility = View.VISIBLE
        }

        if (!::mActivitiesRef.isInitialized) {

            return
        }

        mSnapshotListener =
            mActivitiesRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

                if (querySnapshot != null) {

                    loaderLayout.apply {

                        if (visibility != View.GONE) {

                            visibility = View.GONE
                        }
                    }

                    mActivities.clear()
                    mItemIds.clear()

                    if (querySnapshot.isEmpty) {

                        createNewActivityView.visibility = View.VISIBLE

                        createNewActivityButton.apply {

                            setOnClickListener {

                                findNavController().navigate(R.id.action_showCreateActivityFragment)
                            }
                        }

                        refreshRecyclerView(recyclerView)

                        return@addSnapshotListener
                    }

                    val activities = querySnapshot.documents

                    for (activity in activities) {

                        if (activity.exists()) {

                            val timeoActivity = activity.toObject(TimeoActivity::class.java)

                            if (timeoActivity != null) {

                                mActivities.add(timeoActivity)
                                mItemIds.add(activity.id)
                            }
                        }
                    }

                    refreshRecyclerView(recyclerView)
                } else if (firebaseFirestoreException != null) {

                    Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)
                }
            }
    }

    override fun onPause() {

        super.onPause()

        if (::mSnapshotListener.isInitialized) {

            mSnapshotListener.remove()
        }
    }

    private fun refreshRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter =
            ActivitiesListAdapter(mActivities.toTypedArray(), findNavController(), mItemIds)

        recyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter
        }
    }
}
