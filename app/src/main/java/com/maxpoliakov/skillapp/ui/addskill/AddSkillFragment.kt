package com.maxpoliakov.skillapp.ui.addskill

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.AddskillFragBinding
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.DataBindingFragment
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment : DataBindingFragment<AddskillFragBinding>() {
    override val layoutId get() = R.layout.addskill_frag

    private val viewModel: AddSkillViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }


    override fun onBindingCreated(binding: AddskillFragBinding) {
        binding.viewModel = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val values = UiMeasurementUnit.values().map { unit -> getString(unit.nameResId) }
        binding.unitSelectionSelect.setup(values)
        binding.unitSelectionSelect.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            viewModel.setMeasurementUnitIndex(pos)
            viewModel.setGoal(null)
        }

        observe(viewModel.goToSkillDetail) {
            findNavController().navigateUp()
        }

        observe(viewModel.chooseGoal) {
            viewModel.unit.value!!.showGoalPicker(requireContext(), viewModel.goal.value?.mapToDomain(), onGoalSet = viewModel::setGoal)
        }
    }
}
