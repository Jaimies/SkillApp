package com.maxpoliakov.skillapp.ui.skillgroup

import android.os.Bundle
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

    override val SkillGroupFragBinding.chart get() = productivityChart.chart
    override val content get() = requireBinding().dataLayout
    override val saveBtn get() = requireBinding().saveFab
    override val input get() = requireBinding().titleInput
    override val goalInput get() = requireBinding().goalPicker.root

    override val recyclerView get() = requireBinding().history.recyclerView
    override val history get() = requireBinding().history.root

    override fun onBindingCreated(binding: SkillGroupFragBinding, savedInstanceState: Bundle?) {
        binding.viewModel = viewModel
    }

    override fun onStartEditing() = logEvent("edit_skill_group")
}
