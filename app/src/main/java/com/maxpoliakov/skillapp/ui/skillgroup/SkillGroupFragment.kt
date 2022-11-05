package com.maxpoliakov.skillapp.ui.skillgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.viewModels
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.SkillGroupFragBinding
import com.maxpoliakov.skillapp.ui.common.DetailsFragment
import com.maxpoliakov.skillapp.util.analytics.logEvent
import com.maxpoliakov.skillapp.util.fragment.observe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SkillGroupFragment : DetailsFragment(R.menu.skillgroup_detail_frag_menu) {
    private lateinit var binding: SkillGroupFragBinding

    override val viewModel: SkillGroupViewModel by viewModels()

    override val chart get() = binding.productivityChart.chart

    override val content get() = binding.dataLayout
    override val saveBtn get() = binding.saveFab
    override val input get() = binding.titleInput
    override val goalInput get() = binding.goalPicker.root

    override val recyclerView get() = binding.history.recyclerView
    override val history get() = binding.history.root

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = SkillGroupFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onStartEditing() = logEvent("edit_skill_group")
}
