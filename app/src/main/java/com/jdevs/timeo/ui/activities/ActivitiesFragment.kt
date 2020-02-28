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
import com.jdevs.timeo.ui.activitydetail.ActivityDetailFragmentArgs
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.showTimePicker
import com.jdevs.timeo.util.time.getMins
import kotlinx.android.synthetic.main.history_frag.recycler_view
import javax.inject.Inject

private const val ACTIVITIES_VISIBLE_THRESHOLD = 5

class ActivitiesFragment : ListFragment<ActivityItem>() {

    override val delegateAdapter by lazy {
        ActivityDelegateAdapter(::showRecordDialog, ::navigateToDetails)
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

        val binding = ActivitiesFragBinding.inflate(inflater, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recycler_view.setup(ACTIVITIES_VISIBLE_THRESHOLD)

        observeEvent(viewModel.navigateToAddEdit) {
            findNavController().navigateAnimated(R.id.addactivity_fragment_dest)
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu) {

        if (isLoadEventHandled) {
            return
        }

        menu.forEach { menuItem -> menuItem.isEnabled = false }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->

            if (isLoading || isLoadEventHandled) {
                return@observe
            }

            menu.forEach { menuItem -> menuItem.isEnabled = true }
            isLoadEventHandled = true
        }
    }

    private fun navigateToDetails(index: Int) {

        val args = ActivityDetailFragmentArgs(getItem(index)).toBundle()
        findNavController().navigateAnimated(R.id.activity_detail_fragment_dest, args)
    }

    private fun showRecordDialog(index: Int) {

        showTimePicker { hour, minute ->

            viewModel.createRecord(activity = getItem(index), time = getMins(hour, minute))
        }
    }
}
