package com.jdevs.timeo.models

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.R
import com.jdevs.timeo.TAG
import com.jdevs.timeo.data.TimeoActivity

open class ActivitiesListFragment : ActionBarFragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mActivities = ArrayList<TimeoActivity>()
    private val mItemIds = ArrayList<String>()

    private lateinit var mActivitiesRef : Query

    private lateinit var mViewAdapter : ActivitiesListAdapter
    lateinit var mRecyclerView: RecyclerView

    lateinit var mCreateNewActivityView : LinearLayout
    lateinit var mCreateNewActivityButton : MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivitiesRef = mFirestore
            .collection("users/${mUser.uid}/activities")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(20)

    }


    fun setupActivityListener(recyclerView: RecyclerView) {

        mActivitiesRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if(querySnapshot != null) {

                if(querySnapshot.isEmpty) {

                    if(::mCreateNewActivityView.isInitialized) {

                        mCreateNewActivityView.visibility = View.VISIBLE


                        if(::mCreateNewActivityButton.isInitialized) {

                            mCreateNewActivityButton.apply {

                                setOnClickListener {

                                    findNavController().navigate(R.id.action_showCreateActivityFragment)

                                }

                            }

                        }

                    }

                    return@addSnapshotListener

                }


                val activities = querySnapshot.documents

                mActivities.clear()
                mItemIds.clear()



                for(activity in activities) {

                    if(activity.exists()) {


                        val timeoActivity = activity.toObject(TimeoActivity::class.java)

                        if(timeoActivity != null) {

                            mActivities.add(timeoActivity)
                            mItemIds.add(activity.id)

                        }

                    }

                }

                refreshRecyclerView(recyclerView)


            } else if(firebaseFirestoreException != null) {

                Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)

            }

        }

    }



    private fun refreshRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter = ActivitiesListAdapter(mActivities.toTypedArray(), findNavController(), mItemIds)


        recyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter

        }

    }

}