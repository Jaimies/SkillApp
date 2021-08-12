package com.maxpoliakov.skillapp.ui.skillgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.CollapsingToolbarBinding
import com.maxpoliakov.skillapp.databinding.SkillGroupFragBinding
import com.maxpoliakov.skillapp.ui.common.FragmentWithCollapsingToolbar
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.setState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SkillGroupFragment : FragmentWithCollapsingToolbar(R.menu.skilldetail_frag_menu) {
    private lateinit var binding: SkillGroupFragBinding
    private val args: SkillGroupFragmentArgs by navArgs()

    @Inject
    lateinit var viewModelFactory: SkillGroupViewModel.Factory

    private val viewModel by viewModels { viewModelFactory.create(args.groupId) }

    override val collapsingToolbarBinding: CollapsingToolbarBinding
        get() = binding.collapsingToolbarLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SkillGroupFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.productivityChart.chart.setup()
        observe(viewModel.stats, binding.productivityChart.chart::setState)
    }
}
