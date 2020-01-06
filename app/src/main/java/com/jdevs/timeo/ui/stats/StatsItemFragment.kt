package com.jdevs.timeo.ui.stats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.databinding.StatsPageFragBinding
import com.jdevs.timeo.model.DayStats
import com.jdevs.timeo.util.StatsConstants.VISIBLE_THRESHOLD
import javax.inject.Inject

class StatsItemFragment(private val statsType: Int) : ListFragment<DayStats>() {

    override val menuId = -1

    override val adapter by lazy { StatsRecyclerViewAdapter() }
    override val firestoreAdapter by lazy { FirestoreStatsAdapter() }

    @Inject
    override lateinit var viewModel: StatsViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.setStatsType(statsType)

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = StatsPageFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this

            it.recyclerView.setup(VISIBLE_THRESHOLD)
        }

        return binding.root
    }
}
