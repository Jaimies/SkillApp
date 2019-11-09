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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.jdevs.timeo.data.TimeoRecord
import com.jdevs.timeo.models.ActionBarFragment
import com.jdevs.timeo.models.ActivitiesListFragment
import com.jdevs.timeo.models.RealtimeScrollListener
import com.jdevs.timeo.models.RecordsListAdapter
import kotlinx.android.synthetic.main.partial_circular_loader.view.*
import kotlinx.android.synthetic.main.partial_records_list.view.*

class HistoryFragment : ActionBarFragment(),
    EventListener<QuerySnapshot> {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mRecords = ArrayList<TimeoRecord>()
    private val mItemIds = ArrayList<String>()

    private lateinit var mRecordsCollection: CollectionReference
    private lateinit var mRecordsRef: Query

    private lateinit var mViewAdapter: RecordsListAdapter

    private lateinit var mLoader: FrameLayout
    private lateinit var mRecordsRecyclerView: RecyclerView
    private lateinit var mCreateNewActivityView: TextView
    private val mAuth = FirebaseAuth.getInstance()

    private var isNewDataAvailable = true

    private var lastLoadedDocument: DocumentSnapshot? = null

    private val mListener by lazy {
        EventListener<QuerySnapshot> { querySnapshot, firebaseFirestoreException ->

            if (querySnapshot != null) {

                mLoader.apply {

                    if (visibility != View.GONE) {

                        visibility = View.GONE
                    }
                }

                if (querySnapshot.isEmpty) {

                    if (mRecords.isEmpty()) {

                        mCreateNewActivityView.visibility = View.VISIBLE

                        mViewAdapter.notifyDataSetChanged()
                    }

                    isNewDataAvailable = false

                    return@EventListener
                }

                val activities = querySnapshot.documents

                for ((index, activity) in activities.withIndex()) {

                    if (activity.exists()) {

                        val timeoRecord = activity.toObject(TimeoRecord::class.java)

                        if (timeoRecord != null) {

                            mRecords.add(timeoRecord)
                            mItemIds.add(activity.id)

                            mViewAdapter.notifyItemChanged(index)
                        }
                    }
                }

                lastLoadedDocument = activities.last()
                isNewDataAvailable = true
            } else if (firebaseFirestoreException != null) {

                Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)
            }
        }
    }

    private val mSnapshotListeners = ArrayList<ListenerRegistration>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mRecordsCollection = mFirestore.collection("users/${mAuth.currentUser!!.uid}/records")

        mRecordsRef = mRecordsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(ONE_FETCH_ITEMS_MAX_COUNT)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_history, container, false)

        mLoader = view.spinningProgressBar

        mRecordsRecyclerView = view.recordsRecyclerView

        mRecordsRecyclerView.addOnScrollListener(RealtimeScrollListener(::loadItems))

        mCreateNewActivityView = view.createNewActivityTextView

        // Inflate the layout for this fragment
        return view
    }

    override fun onStart() {
        super.onStart()

        mLoader.apply {

            visibility = View.VISIBLE
        }

        mRecordsRef.addSnapshotListener(requireActivity(), this)
    }

    override fun onEvent(
        querySnapshot: QuerySnapshot?,
        firebaseFirestoreException: FirebaseFirestoreException?
    ) {

        if (querySnapshot != null) {

            mLoader.apply {

                if (visibility != View.GONE) {

                    visibility = View.GONE
                }
            }

            mRecords.clear()
            mItemIds.clear()

            if (querySnapshot.isEmpty) {

                mCreateNewActivityView.visibility = View.VISIBLE

                mViewAdapter.notifyDataSetChanged()

                return
            }

            val records = querySnapshot.documents

            displayRecords(records)
        } else if (firebaseFirestoreException != null) {

            Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)
        }
    }

    private fun displayRecords(records: List<DocumentSnapshot>) {
        for (record in records) {

            if (record.exists()) {

                val timeoRecord = record.toObject(TimeoRecord::class.java)

                if (timeoRecord != null) {

                    mRecords.add(timeoRecord)
                    mItemIds.add(record.id)
                }
            }
        }

        setupRecyclerView()
    }

    private fun setupRecyclerView() {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter = RecordsListAdapter(
            mRecords,
            mRecordsCollection,
            mItemIds,
            mAuth.currentUser!!.uid,
            context
        )

        mRecordsRecyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter
        }
    }

    private fun loadItems() {

        if (!isNewDataAvailable) {

            return
        }

        val document = lastLoadedDocument

        val listenerRegistration = if (document != null) {

            mRecordsRef
                .startAfter(document)
                .limit(ActivitiesListFragment.ONE_FETCH_ITEMS_MAX_COUNT)
                .addSnapshotListener(mListener)
        } else {

            mRecordsRef
                .limit(ActivitiesListFragment.ONE_FETCH_ITEMS_MAX_COUNT)
                .addSnapshotListener(mListener)
        }

        mSnapshotListeners.add(listenerRegistration)
    }

    companion object {

        const val ONE_FETCH_ITEMS_MAX_COUNT: Long = 20
    }
}
