package com.maxpoliakov.skillapp.ui.skillgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.data.PieEntry
import com.maxpoliakov.skillapp.MainDirections
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.CollapsingToolbarBinding
import com.maxpoliakov.skillapp.databinding.SkillGroupFragBinding
import com.maxpoliakov.skillapp.model.toMinimal
import com.maxpoliakov.skillapp.ui.common.FragmentWithCollapsingToolbar
import com.maxpoliakov.skillapp.util.charts.setData
import com.maxpoliakov.skillapp.util.charts.setup
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.lifecycle.viewModels
import com.maxpoliakov.skillapp.util.ui.navigateAnimated
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
        binding.splitChart.chart.setup()

        observe(viewModel.group) { group ->
            val data = group.skills.map { skill ->
                PieEntry(skill.totalTime.toMillis().toFloat(), skill.name)
            }.sortedBy { -it.y }

            binding.splitChart.chart.setData(data)
        }
        observe(viewModel.stats, binding.productivityChart.chart::setState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.edit) {
            val directions = MainDirections.actionToEditSkillGroupFragment(viewModel.group.value!!.toMinimal())
            findNavController().navigateAnimated(directions)
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
