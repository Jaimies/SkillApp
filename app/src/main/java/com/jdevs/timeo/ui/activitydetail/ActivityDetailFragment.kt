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
import com.jdevs.timeo.ui.stats.setupTabLayoutMediator
import com.jdevs.timeo.util.extensions.appComponent
import com.jdevs.timeo.util.extensions.observeEvent
import com.jdevs.timeo.util.extensions.setupAdapter
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
        viewModel.setActivity(args.activity)
        viewModel.setupActivityLiveData(args.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel
            it.statsViewpager.adapter = ActivityDetailStatsAdapter { statsType ->

                val directions = ActivityDetailFragmentDirections
                    .actionActivityDetailFragmentToStatsFragment(statsType)

                findNavController().navigate(directions)
            }

            setupTabLayoutMediator(it.statsTablayout, it.statsViewpager)
            it.achievementsList.setupAdapter(adapter, RecyclerView.HORIZONTAL)
            it.achievementsList.addItemDecoration(SpaceItemDecoration(ACHIEVEMENTS_ITEM_SPACING))
        }

        viewModel.apply {

            activity.observe(viewLifecycleOwner) { activity -> setActivity(activity) }

            observeEvent(showRecordDialog) {
                RecordDialog(context!!) { time -> addRecord(activity.value!!, time) }.show()
            }
        }

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return if (item.itemId == R.id.editActivity) {

            val directions = ActivityDetailFragmentDirections
                .actionActivityDetailFragmentToAddEditActivityFragment(viewModel.activity.value!!)

            findNavController().navigate(directions)
            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private const val ACHIEVEMENTS_ITEM_SPACING = 20
    }
}
