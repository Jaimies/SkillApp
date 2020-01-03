package com.jdevs.timeo.ui.graphs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.jdevs.timeo.TimeoApplication
import com.jdevs.timeo.common.ListFragment
import com.jdevs.timeo.data.DayStats
import com.jdevs.timeo.databinding.GraphsItemFragBinding
import com.jdevs.timeo.util.StatsConstants.VISIBLE_THRESHOLD
import javax.inject.Inject

class GraphsItemFragment(private val type: Int) : ListFragment<DayStats>() {

    override val menuId = -1

    override val adapter by lazy { GraphsRecyclerViewAdapter() }
    override val firestoreAdapter by lazy { FirestoreGraphsAdapter() }

    @Inject
    override lateinit var viewModel: GraphsViewModel

    override fun onAttach(context: Context) {

        super.onAttach(context)
        (activity!!.application as TimeoApplication).appComponent.inject(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewModel.setGraphType(type)

        super.onCreateView(inflater, container, savedInstanceState)

        val binding = GraphsItemFragBinding.inflate(inflater, container, false).also {

            it.viewModel = viewModel
            it.lifecycleOwner = this

            it.recyclerView.apply {

                layoutManager = LinearLayoutManager(context)
                adapter = GraphsRecyclerViewAdapter()
                setup(VISIBLE_THRESHOLD)
            }
        }

        return binding.root
    }
}
