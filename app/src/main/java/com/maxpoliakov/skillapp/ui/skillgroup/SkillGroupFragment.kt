package com.maxpoliakov.skillapp.ui.skillgroup

import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillGroupFragment : DetailsFragment<SkillGroupFragBinding>(R.menu.skillgroup_detail_frag_menu) {
    override val layoutId get() = R.layout.skill_group_frag

    override val viewModel: SkillGroupViewModel by viewModels()

    override val chart get() = binding.productivityChart.chart
    override val content get() = binding.dataLayout
    override val saveBtn get() = binding.saveFab
    override val input get() = binding.titleInput
    override val goalInput get() = binding.goalPicker.root

    override val recyclerView get() = binding.history.recyclerView
    override val history get() = binding.history.root

    override fun onBindingCreated(binding: SkillGroupFragBinding) {
        binding.viewModel = viewModel
    }

    override fun onStartEditing() = logEvent("edit_skill_group")
}
