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
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.data.Activity
import com.jdevs.timeo.data.Record
import com.jdevs.timeo.databinding.ActivitiesFragBinding
import com.jdevs.timeo.util.ActivitiesConstants
import com.jdevs.timeo.util.observeEvent
import javax.inject.Inject

class ActivitiesFragment : ListFragment<Activity>() {

    override val roomAdapter by lazy { ActivitiesAdapter(::createRecord, ::navigateToDetails) }
    override val firestoreAdapter by lazy {
        FirestoreActivitiesAdapter(::createRecord, ::navigateToDetails)
    }

    override val menuId = R.menu.activities_fragment_menu
    private lateinit var menu: Menu
    private var isLoadEventHandled = false

    @Inject
    override lateinit var viewModel: ActivitiesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = ActivitiesFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this
            it.recyclerView.setup(ActivitiesConstants.VISIBLE_THRESHOLD)
        }

        observeEvent(viewModel.navigateToAddEdit) {

            findNavController().navigate(R.id.action_activitiesFragment_to_addEditActivityFragment)
        }

        return binding.root
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        this.menu = menu

        if (!isLoadEventHandled) {

            menu.forEach {

                it.isEnabled = false
            }

            viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

                if (!isLoadEventHandled && !isLoading) {

                    menu.forEach { it.isEnabled = true }
                    isLoadEventHandled = true
                }
            }
        }

        super.onPrepareOptionsMenu(menu)
    }

    private fun navigateToDetails(index: Int) {

        val action = ActivitiesFragmentDirections
            .actionActivitiesFragmentToActivityDetailsFragment(
                activity = getItem(index)
            )

        findNavController().navigate(action)
    }

    private fun createRecord(index: Int, time: Long) {

        val activity = getItem(index)

        viewModel.createRecord(
            Record(
                name = activity.name,
                time = time,
                activityId = activity.documentId,
                roomActivityId = activity.id
            )
        )
    }
}
