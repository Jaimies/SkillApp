package com.theskillapp.skillapp.ui.addskill

import android.os.Bundle
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.theskillapp.skillapp.R
import com.theskillapp.skillapp.databinding.AddskillFragBinding
import com.theskillapp.skillapp.model.UiMeasurementUnit
import com.theskillapp.skillapp.model.mapToDomain
import com.theskillapp.skillapp.shared.DataBindingFragment
import com.theskillapp.skillapp.shared.fragment.observe
import com.theskillapp.skillapp.shared.transition.createMaterialContainerTransform
import com.theskillapp.skillapp.shared.extensions.setup
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


    override fun onBindingCreated(binding: AddskillFragBinding, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)
        binding.viewModel = viewModel

        val values = UiMeasurementUnit.values().map { unit -> getString(unit.nameResId) }
        binding.unitSelectionSelect.setup(values)
        binding.unitSelectionSelect.onItemClickListener = AdapterView.OnItemClickListener { _, _, pos, _ ->
            viewModel.setMeasurementUnitIndex(pos)
            viewModel.setGoal(null)
        }

        observe(viewModel.navigateUp) {
            findNavController().navigateUp()
        }

        observe(viewModel.chooseGoal) {
            viewModel.unit.value!!.showGoalPicker(childFragmentManager, viewModel.goal.value?.mapToDomain(), onGoalSet = viewModel::setGoal)
        }
    }
}
