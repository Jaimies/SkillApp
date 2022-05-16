package com.maxpoliakov.skillapp.ui.skillgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillGroupFragment : DetailsFragment(R.menu.skillgroup_detail_frag_menu) {
    private lateinit var binding: SkillGroupFragBinding
    private val args: SkillGroupFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: SkillGroupViewModel.Factory

    override val viewModel by viewModels { viewModelFactory.create(args.groupId) }

    override val chart get() = binding.productivityChart.chart

    override val content get() = binding.dataLayout
    override val saveBtn get() = binding.saveFab
    override val input get() = binding.titleInput
    override val goalInput get() = binding.goalPicker.root

    override val recyclerView get() = binding.history.recyclerView
    override val emptyListLayout get() = binding.history.emptyListLayout.root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SkillGroupFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(viewModel.chartData.statisticType) { type ->
            binding.productivityChart.chart.update(type, viewModel.chartData, viewLifecycleOwner)
        }

        observe(viewModel.group) { group ->
            val data = group.skills.map { skill ->
                PieEntry(skill.totalCount.toFloat(), skill.name)
            }

            binding.splitChart.chart.setData(data)
            binding.splitChart.root.isGone = binding.splitChart.chart.data == null
        }
    }

    override fun onStartEditing() = logEvent("edit_skill_group")
}
