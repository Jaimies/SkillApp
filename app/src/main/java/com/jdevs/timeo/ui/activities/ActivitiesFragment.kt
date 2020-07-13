package com.jdevs.timeo.ui.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.jdevs.timeo.OverviewDirections
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitiesFragBinding
import com.jdevs.timeo.model.ActivityItem
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.ui.navigateAnimated
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.history_frag.recycler_view

private const val ACTIVITIES_VISIBLE_THRESHOLD = 5

@AndroidEntryPoint
class ActivitiesFragment : ListFragment<ActivityItem>(R.menu.activities_frag_menu) {

    override val delegateAdapter by lazy {
        ActivityDelegateAdapter(::showRecordDialog, ::navigateToDetails)
    }

    override val viewModel: ActivitiesViewModel by viewModels()
    private var isLoadEventHandled = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ActivitiesFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        recycler_view.setup(ACTIVITIES_VISIBLE_THRESHOLD)

        observe(viewModel.navigateToAddEdit) {
            findNavController().navigateAnimated(R.id.addactivity_fragment_dest)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        if (isLoadEventHandled) {
            return
        }

        menu.forEach { menuItem -> menuItem.isEnabled = false }

        observe(viewModel.isLoading) { isLoading ->

            if (isLoading || isLoadEventHandled) {
                return@observe
            }

            menu.forEach { menuItem -> menuItem.isEnabled = true }
            isLoadEventHandled = true
        }
    }

    private fun navigateToDetails(index: Int) {
        val directions = OverviewDirections.actionToActivityDetailFragment(getItem(index).id)
        findNavController().navigateAnimated(directions)
    }

    private fun showRecordDialog(index: Int) {
        showTimePicker { hours, minutes ->
            viewModel.createRecord(getItem(index), getMins(hours, minutes))
        }
    }
}
