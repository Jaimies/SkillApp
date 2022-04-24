package com.maxpoliakov.skillapp.ui.addskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.AddskillFragBinding
import com.maxpoliakov.skillapp.model.UiMeasurementUnit
import com.maxpoliakov.skillapp.ui.common.BaseFragment
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.setup
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment : BaseFragment() {
    private val viewModel: AddSkillViewModel by viewModels()
    private lateinit var binding: AddskillFragBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AddskillFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
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
            viewModel.unit.showGoalPicker(requireContext(), viewModel.goal.value, onGoalSet = viewModel::setGoal)
        }
    }
}
