package com.jdevs.timeo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.RecordsListAdapter
import kotlinx.android.synthetic.main.partial_records_list.view.*


class HistoryFragment : ActionBarFragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mRecords = ArrayList<TimeoRecord>()

    private lateinit var mActivitiesRef : Query

    private lateinit var mViewAdapter : RecordsListAdapter
    private lateinit var mRecyclerView: RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = auth.currentUser

        if(user != null) {

            mActivitiesRef = mFirestore
                .collection("users/${user.uid}/records")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(30)

        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        mRecyclerView  = view.recordsRecyclerView

        // Inflate the layout for this fragment
        return view

    }



    override fun onStart() {
        super.onStart()


        mActivitiesRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            if(querySnapshot != null && !querySnapshot.isEmpty) {

                val records = querySnapshot.documents

                mRecords.clear()

                for(record in records) {

                    if(record.exists()) {


                        val timeoRecord = record.toObject(TimeoRecord::class.java)

                        if(timeoRecord != null) {

                            mRecords.add(timeoRecord)

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

        val viewManager = LinearLayoutManager(context)

        mViewAdapter = RecordsListAdapter(mRecords.toTypedArray())


        mRecyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter

        }

    }


}