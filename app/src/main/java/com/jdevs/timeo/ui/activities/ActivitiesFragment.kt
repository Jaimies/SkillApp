package com.jdevs.timeo.ui.activities

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitiesFragBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.observeEvent
import javax.inject.Inject

private const val ACTIVITIES_VISIBLE_THRESHOLD = 5

class ActivitiesFragment : ListFragment<ActivityItem>() {

    override val adapter by lazy {
        PagingAdapter(ActivityDelegateAdapter(), ::createRecord, ::navigateToDetails)
    }

    override val firestoreAdapter by lazy {
        FirestoreListAdapter(::createRecord, ::navigateToDetails)
    }

    @Inject
    override lateinit var viewModel: ActivitiesViewModel

    override val menuId = R.menu.activities_fragment_menu
    private var isLoadEventHandled = false

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = ActivitiesFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.recyclerView.setup(ACTIVITIES_VISIBLE_THRESHOLD)
        }

        observeEvent(viewModel.navigateToAddEdit) {

            findNavController().navigate(R.id.addactivity_fragment_dest)
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        if (!isLoadEventHandled) {

            menu.forEach { menuItem -> menuItem.isEnabled = false }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

                if (!isLoadEventHandled && !isLoading) {

                    menu.forEach { menuItem -> menuItem.isEnabled = true }
                    isLoadEventHandled = true
                }
            }
        }
    }

    private fun navigateToDetails(index: Int) {

        val directions = ActivitiesFragmentDirections
            .actionActivitiesFragmentToActivityDetailsFragment(activity = getItem(index))

        findNavController().navigate(directions)
    }

    private fun createRecord(index: Int, time: Int) {

        viewModel.createRecord(activity = getItem(index), time = time)
    }
}
