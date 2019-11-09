package com.jdevs.timeo.models

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AbsListView
import android.widget.Button
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.jdevs.timeo.R
import com.jdevs.timeo.TAG
import com.jdevs.timeo.data.TimeoActivity

open class ActivitiesListFragment : ActionBarFragment() {

    private val mFirestore = FirebaseFirestore.getInstance()
    private val mActivities = ArrayList<TimeoActivity>()
    private val mItemIds = ArrayList<String>()

    private lateinit var mActivitiesRef: Query

    private lateinit var mListener: EventListener<QuerySnapshot>
    private lateinit var mViewAdapter: ActivitiesListAdapter

    private var lastLoadedDocument: DocumentSnapshot? = null

    private val mSnapshotListeners = ArrayList<ListenerRegistration>()

    private val mUser = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (mUser == null) {

            return
        }

        mActivitiesRef = mFirestore
            .collection("users/${mUser.uid}/activities")
            .orderBy("timestamp", Query.Direction.DESCENDING)
    }

    fun setupActivityListener(
        recyclerView: RecyclerView,
        loaderLayout: FrameLayout,
        createNewActivityView: LinearLayout,
        createNewActivityButton: Button
    ) {

        loaderLayout.apply {

            visibility = View.VISIBLE
        }

        if (!::mActivitiesRef.isInitialized) {

            return
        }

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            private var isScrolling = false

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {

                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {

                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                (recyclerView.layoutManager as? LinearLayoutManager)?.apply {

                    val lastVisibleItem = findLastVisibleItemPosition()
                    val totalItemsCount = itemCount

                    if (isScrolling && (lastVisibleItem == totalItemsCount - 1)) {

                        isScrolling = false
                        loadItems()

                    }

                }
            }
        })

        mListener =
            EventListener { querySnapshot, firebaseFirestoreException ->

                if (querySnapshot != null) {

                    loaderLayout.apply {

                        if (visibility != View.GONE) {

                            visibility = View.GONE
                        }
                    }

                    if (querySnapshot.isEmpty) {

                        if (mActivities.isEmpty()) {

                            createNewActivityView.visibility = View.VISIBLE

                            createNewActivityButton.apply {

                                setOnClickListener {

                                    findNavController().navigate(R.id.action_showCreateActivityFragment)
                                }
                            }

                            mViewAdapter.notifyDataSetChanged()
                        }

                        return@EventListener
                    }

                    val activities = querySnapshot.documents

                    for ((index, activity) in activities.withIndex()) {

                        if (activity.exists()) {

                            val timeoActivity = activity.toObject(TimeoActivity::class.java)

                            if (timeoActivity != null) {

                                mActivities.add(timeoActivity)
                                mItemIds.add(activity.id)

                                mViewAdapter.notifyItemChanged(index)
                            }
                        }
                    }

                    lastLoadedDocument = activities.last()

                } else if (firebaseFirestoreException != null) {

                    Log.w(TAG, "Failed to get data from Firestore", firebaseFirestoreException)
                }
            }

        setupRecyclerView(recyclerView)

        loadItems()
    }

    override fun onPause() {

        super.onPause()

        mSnapshotListeners.forEach { it.remove() }
    }

    override fun onResume() {
        super.onResume()

        mActivitiesRef = mFirestore
            .collection("users/${mUser!!.uid}/activities")
            .orderBy("timestamp", Query.Direction.DESCENDING)

        lastLoadedDocument = null

        mSnapshotListeners.forEach { it.remove() }

        mActivities.clear()
        mViewAdapter.notifyDataSetChanged()

        loadItems()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {

        val viewManager = LinearLayoutManager(context)

        mViewAdapter =
            ActivitiesListAdapter(mActivities, findNavController(), mItemIds)

        recyclerView.setHasFixedSize(false)

        recyclerView.apply {

            layoutManager = viewManager

            adapter = mViewAdapter
        }
    }

    private fun loadItems() {

        val document = lastLoadedDocument

        val listenerRegistration = if (document != null) {

            mActivitiesRef
                .startAfter(document)
                .limit(ONE_FETCH_ITEMS_MAX_COUNT)
                .addSnapshotListener(mListener)
        } else {

            mActivitiesRef
                .limit(ONE_FETCH_ITEMS_MAX_COUNT)
                .addSnapshotListener(mListener)
        }



        mSnapshotListeners.add(listenerRegistration)
    }

    companion object {

        const val ONE_FETCH_ITEMS_MAX_COUNT: Long = 12
    }
}
