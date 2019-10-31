package com.jdevs.timeo


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.RecordsListAdapter
import kotlinx.android.synthetic.main.partial_circular_loader.view.*
import kotlinx.android.synthetic.main.partial_records_list.view.*


class HistoryFragment : ActionBarFragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mRecords = ArrayList<TimeoRecord>()
    private val mItemIds = ArrayList<String>()

    private lateinit var mRecordsCollection : CollectionReference
    private lateinit var mRecordsSorted: Query

    private lateinit var mViewAdapter : RecordsListAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mCreateNewActivityTextView: TextView

    private lateinit var mLoader : FrameLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRecordsCollection = mFirestore.collection("users/${mUser.uid}/records")

        mRecordsSorted = mRecordsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(30)


    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        mRecyclerView  = view.recordsRecyclerView

        mCreateNewActivityTextView = view.createNewActivityTextView

        mLoader = view.spinningProgressBar

        // Inflate the layout for this fragment
        return view

    }



    override fun onStart() {
        super.onStart()

        mLoader.apply {

            visibility = View.VISIBLE

        }


        mRecordsSorted.addSnapshotListener(requireActivity()) { querySnapshot, firebaseFirestoreException ->

            if(querySnapshot != null) {

                mLoader.apply {

                    if(visibility != View.GONE) {

                        visibility  = View.GONE

                    }

                }


                mRecords.clear()
                mItemIds.clear()


                if(querySnapshot.isEmpty) {

                    mCreateNewActivityTextView.visibility = View.VISIBLE

                    refreshRecyclerView()

                    return@addSnapshotListener

                }


                val records = querySnapshot.documents

                for(record in records) {

                    if(record.exists()) {


                        val timeoRecord = record.toObject(TimeoRecord::class.java)

                        if(timeoRecord != null) {

                            mRecords.add(timeoRecord)
                            mItemIds.add(record.id)

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

        mViewAdapter = RecordsListAdapter(
            mRecords.toTypedArray(),
            mRecordsCollection,
            mItemIds,
            context
        )

        mRecyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter

        }

    }


}