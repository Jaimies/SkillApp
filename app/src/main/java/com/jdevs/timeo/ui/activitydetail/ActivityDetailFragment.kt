package com.jdevs.timeo.ui.activitydetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.ui.common.adapter.SpaceItemDecoration
import com.jdevs.timeo.util.appComponent
import com.jdevs.timeo.util.observeEvent
import com.jdevs.timeo.util.setup
import com.jdevs.timeo.util.setupAdapter
import kotlinx.android.synthetic.main.activitydetail_frag.achievements_list
import kotlinx.android.synthetic.main.activitydetail_frag.lineChart
import javax.inject.Inject

class ActivityDetailFragment : ActionBarFragment() {

    @Inject
    lateinit var viewModel: ActivityDetailViewModel

    override val menuId = R.menu.activity_detail_fragment_menu
    private val args: ActivityDetailFragmentArgs by navArgs()
    private val adapter by lazy { AchievementsAdapter() }

    override fun onAttach(context: Context) {

        super.onAttach(context)
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setupActivityLiveData(args.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lineChart.setup(CHART_TEXT_SIZE, CHART_OFFSET)

        achievements_list.setupAdapter(adapter, RecyclerView.HORIZONTAL)
        achievements_list.addItemDecoration(SpaceItemDecoration(ACHIEVEMENTS_ITEM_SPACING))

        viewModel.run {

            activity.observe(viewLifecycleOwner, viewModel::setActivity)

            observeEvent(showRecordDialog) {
                RecordDialog(requireContext()) { time -> addRecord(activity.value!!, time) }.show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {

            val directions = ActivityDetailFragmentDirections
                .actionActivityDetailFragmentToAddEditActivityFragment(viewModel.activity.value!!)

            findNavController().navigate(directions)
            return true
        }

        return false
    }

    companion object {
        private const val ACHIEVEMENTS_ITEM_SPACING = 20
        private const val CHART_TEXT_SIZE = 16f
        private const val CHART_OFFSET = 8f
    }
}
