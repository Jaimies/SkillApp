package com.jdevs.timeo.ui.activitydetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.jdevs.timeo.R
import com.jdevs.timeo.databinding.ActivitydetailFragBinding
import com.jdevs.timeo.ui.addactivity.AddEditActivityFragmentArgs
import com.jdevs.timeo.ui.common.ActionBarFragment
import com.jdevs.timeo.ui.common.adapter.SpaceItemDecoration
import com.jdevs.timeo.util.charts.setup
import com.jdevs.timeo.util.fragment.appComponent
import com.jdevs.timeo.util.fragment.observe
import com.jdevs.timeo.util.fragment.showTimePicker
import com.jdevs.timeo.util.navigateAnimated
import com.jdevs.timeo.util.time.getMins
import com.jdevs.timeo.util.view.setupAdapter
import kotlinx.android.synthetic.main.activitydetail_frag.achievements_list
import kotlinx.android.synthetic.main.activitydetail_frag.lineChart
import javax.inject.Inject

class ActivityDetailFragment : ActionBarFragment() {

    @Inject
    lateinit var viewModel: ActivityDetailViewModel

    override val menuId = R.menu.activitydetail_frag_menu
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

        val binding = ActivitydetailFragBinding.inflate(inflater, container, false).also {

            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        lineChart.setup(CHART_TEXT_SIZE, CHART_OFFSET)

        achievements_list.setupAdapter(adapter, RecyclerView.HORIZONTAL)
        achievements_list.addItemDecoration(SpaceItemDecoration(ACHIEVEMENTS_ITEM_SPACING))

        observe(viewModel.activity, viewModel.activityData::setData)
        observe(viewModel.showRecordDialog) { showTimePicker(::addRecord) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.editActivity) {

            val args = AddEditActivityFragmentArgs(viewModel.activity.value!!).toBundle()
            findNavController().navigateAnimated(R.id.addactivity_fragment_dest, args)
            return true
        }

        return false
    }

    private fun addRecord(hour: Int, minute: Int) {

        viewModel.addRecord(viewModel.activity.value!!, getMins(hour, minute))
    }

    companion object {
        private const val ACHIEVEMENTS_ITEM_SPACING = 20
        private const val CHART_TEXT_SIZE = 14f
        private const val CHART_OFFSET = 8f
    }
}
