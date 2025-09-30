package com.theskillapp.skillapp.ui.skillgroup

import android.os.Bundle
import androidx.fragment.app.viewModels
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.SkillGroupFragBinding
import com.theskillapp.skillapp.shared.DetailsFragment
import com.theskillapp.skillapp.shared.time.DateFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

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

    @Inject
    lateinit var dateFormatter: DateFormatter

    override fun onBindingCreated(binding: SkillGroupFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel
        binding.dateFormatter = dateFormatter
    }
}
