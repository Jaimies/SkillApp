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
import com.jdevs.timeo.R
import com.jdevs.timeo.common.ActionBarFragment
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.ui.activities.RecordDialog
import com.jdevs.timeo.ui.stats.setupTabLayoutMediator
import com.jdevs.timeo.util.extensions.getAppComponent
import com.jdevs.timeo.util.extensions.observeEvent
import javax.inject.Inject

class ActivityDetailFragment : ActionBarFragment() {

    override val menuId = R.menu.activity_detail_fragment_menu
    private val args: ActivityDetailFragmentArgs by navArgs()

    @Inject
    lateinit var viewModel: ActivityDetailViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        viewModel.setActivity(args.activity)
        viewModel.setupActivityLiveData(args.activity)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = this
            it.viewModel = viewModel

            it.statsViewpager.adapter = ActivityDetailStatsAdapter(this) { statsType ->

                val directions = ActivityDetailFragmentDirections
                    .actionActivityDetailFragmentToStatsFragment(statsType)

                findNavController().navigate(directions)
            }

            setupTabLayoutMediator(it.statsTablayout, it.statsViewpager)
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
                .actionActivityDetailFragmentToAddEditActivityFragment(activity = viewModel.activity.value!!)

            findNavController().navigate(directions)

            true
        } else {

            super.onOptionsItemSelected(item)
        }
    }
}
