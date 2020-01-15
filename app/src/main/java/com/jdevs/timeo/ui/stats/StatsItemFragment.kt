package com.jdevs.timeo.ui.stats

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jdevs.timeo.databinding.StatsPageFragBinding
import com.jdevs.timeo.domain.model.DayStats
import com.jdevs.timeo.ui.common.ListFragment
import com.jdevs.timeo.ui.common.adapter.FirestoreListAdapter
import com.jdevs.timeo.ui.common.adapter.PagingAdapter
import com.jdevs.timeo.util.StatsConstants.VISIBLE_THRESHOLD
import com.jdevs.timeo.util.StatsTypes.DAY
import com.jdevs.timeo.util.extensions.getAppComponent
import javax.inject.Inject

class StatsItemFragment : ListFragment<DayStats>() {

    override val adapter by lazy { PagingAdapter(StatisticDelegateAdapter()) }
    override val firestoreAdapter by lazy { FirestoreListAdapter() }
    override val menuId = -1

    @Inject
    override lateinit var viewModel: StatsViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        getAppComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        viewModel.setStatsType(arguments?.getInt(TYPE) ?: DAY)
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = StatsPageFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this

            it.recyclerView.setup(VISIBLE_THRESHOLD)
        }

        return binding.root
    }

    companion object {

        const val TYPE = "type"
    }
}
