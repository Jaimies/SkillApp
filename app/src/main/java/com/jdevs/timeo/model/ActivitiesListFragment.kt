package com.jdevs.timeo.model

import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.jdevs.timeo.TAG
import com.jdevs.timeo.data.TimeoActivity

open class ActivitiesListFragment : ActionBarFragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mActivities = ArrayList<TimeoActivity>()

    private lateinit var mActivitiesRef : CollectionReference

    private lateinit var mViewAdapter : ActivitiesListAdapter
    private lateinit var mRecyclerView: RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mActivitiesRef = mFirestore.collection("users/${auth.currentUser!!.uid}/activities")


    }

    override fun onStart() {
        super.onStart()

        mActivitiesRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if(querySnapshot != null && !querySnapshot.isEmpty) {

                val activities = querySnapshot.documents

                mActivities.clear()

                for(activity in activities) {

                    if(activity.exists()) {


                        val timeoActivity = activity.toObject(TimeoActivity::class.java)

                        if(timeoActivity != null) {

                            mActivities.add(timeoActivity)

                        }

                    }

                }

                refreshRecyclerView()


            } else if(firebaseFirestoreException != null) {

                Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)

            }

        }
    }


    private fun refreshRecyclerView() {

        if(!::mRecyclerView.isInitialized) {

            return

        }

        val viewManager = LinearLayoutManager(context)

        mViewAdapter = ActivitiesListAdapter(mActivities.toTypedArray())


        mRecyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter

        }

    }

    fun initializeRecyclerView(recyclerView: RecyclerView) {

        mRecyclerView = recyclerView

    }

}