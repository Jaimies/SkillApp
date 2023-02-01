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

    override val SkillGroupFragBinding.content get() = dataLayout
    override val SkillGroupFragBinding.saveBtn get() = saveFab
    override val SkillGroupFragBinding.input get() = titleInput
    override val SkillGroupFragBinding.goalInput get() = goalPicker.root
    override val SkillGroupFragBinding.history get() = history.root
    override val SkillGroupFragBinding.recyclerView get() = history.recyclerView

    override fun onBindingCreated(binding: SkillGroupFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
    }

    override fun onStartEditing() = logEvent("edit_skill_group")
}
