package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.R
import com.jdevs.timeo.common.InfiniteScrollListener
import com.jdevs.timeo.common.ItemListFragment
import com.jdevs.timeo.data.TimeoActivity
import com.jdevs.timeo.databinding.FragmentActivityListBinding
import com.jdevs.timeo.ui.activities.adapter.ActivitiesAdapter
import com.jdevs.timeo.ui.activities.viewmodel.ActivityListViewModel
import com.jdevs.timeo.util.ActivitiesConstants

class ActivityListFragment : ItemListFragment<TimeoActivity>(),
    ActivityListViewModel.Navigator {

    override val menuId = R.menu.action_bar_activity_list
    override val mAdapter by lazy { ActivitiesAdapter(::createRecord, ::navigateToDetails) }

    override val viewModel by lazy {
        ViewModelProviders.of(this).get(ActivityListViewModel::class.java).also {
            it.navigator = this
        }
    }

    private lateinit var menu: Menu
    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentActivityListBinding.inflate(inflater, container, false).also {

            it.viewmodel = viewModel
            it.lifecycleOwner = this

            it.recyclerView.apply {

                linearLayoutManager = LinearLayoutManager(context)

                layoutManager = linearLayoutManager
                adapter = mAdapter

                addOnScrollListener(
                    InfiniteScrollListener(
                        ::getItems,
                        linearLayoutManager,
                        ActivitiesConstants.VISIBLE_THRESHOLD
                    )
                )
            }
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        this.menu = menu

        if (!isLoaded) {

            menu.forEach {

                it.isEnabled = false
            }
        }

        super.onPrepareOptionsMenu(menu)
    }

    override fun onItemsLoaded() {

        menu.forEach {

            it.isEnabled = true
        }

        isLoaded = true
    }

    override fun createActivity() {

        findNavController().navigate(R.id.action_showCreateActivityFragment)
    }

    override fun getItems() {

        observe(viewModel.liveData)
    }

    private fun navigateToDetails(index: Int) {

        val activityId = mAdapter.getId(index)
        val item = getActivity(index)

        val action =
            ActivityListFragmentDirections.actionShowActivityDetails(item, activityId)

        findNavController().navigate(action)
    }

    private fun createRecord(index: Int, time: Long) {

        viewModel.createRecord(getActivity(index).name, time, mAdapter.getId(index))
    }

    private fun getActivity(index: Int) = mAdapter.getItem(index) as TimeoActivity
}
