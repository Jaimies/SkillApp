package com.maxpoliakov.skillapp.ui.addskill

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.databinding.AddskillFragBinding
import com.maxpoliakov.skillapp.ui.common.BaseFragment
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.GoalPicker
import com.maxpoliakov.skillapp.util.ui.getFragmentManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSkillFragment : BaseFragment() {
    private val viewModel: AddSkillViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = AddskillFragBinding.inflate(inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        observe(viewModel.goToSkillDetail) { skillId ->
            findNavController().navigateUp()
        }

        observe(viewModel.chooseGoal) {
            val picker = GoalPicker.Builder().build()

            picker.show(requireContext().getFragmentManager(), null)
            picker.addOnPositiveButtonClickListener(View.OnClickListener {
                viewModel.setGoal(picker.goal)
            })
        }
    }
}
