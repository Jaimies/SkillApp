package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import androidx.activity.addCallback
import androidx.annotation.CallSuper
import androidx.annotation.MenuRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.maxpoliakov.skillapp.R
import com.maxpoliakov.skillapp.model.mapToDomain
import com.maxpoliakov.skillapp.ui.common.history.FragmentWithHistory
import com.maxpoliakov.skillapp.util.fragment.observe
import com.maxpoliakov.skillapp.util.hardware.hideKeyboard
import com.maxpoliakov.skillapp.util.hardware.showKeyboard
import com.maxpoliakov.skillapp.util.transition.createMaterialContainerTransform
import com.maxpoliakov.skillapp.util.ui.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

abstract class DetailsFragment<T : ViewDataBinding>(@MenuRes menuId: Int) : FragmentWithHistory<T>(menuId) {
    protected abstract val T.saveBtn: Button
    protected abstract val T.input: EditText
    protected abstract val T.content: ViewGroup
    protected abstract val T.goalInput: View
    protected abstract val T.history: View

    abstract override val viewModel: DetailsViewModel

    protected open fun onDeleteSelected() {}

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = createMaterialContainerTransform(R.integer.animation_duration)
        sharedElementReturnTransition = createMaterialContainerTransform(R.integer.animation_duration_short)
    }

    override fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.run {
            saveBtn.isGone = true
            input.isSingleLine = true
            input.maxLines = 2
            input.setHorizontallyScrolling(false)
            input.makeNonEditable()
        }

        lifecycleScope.launch {
            viewModel.mode.collect(::switchToMode)
        }

        observe(viewModel.onSave) { this.binding?.switchToViewMode() }
        observe(viewModel.navigateUp) { findNavController().navigateUp() }

        observe(viewModel.chooseGoal) {
            viewModel.unit.value!!.showGoalPicker(
                childFragmentManager,
                viewModel.goal.value?.mapToDomain(),
                onGoalSet = viewModel::setGoal
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.onBackPressed()
        }
    }

    override fun onHistoryEmpty() {
        binding?.history?.isVisible = false
    }

    override fun onHistoryNotEmpty() {
        binding?.history?.isVisible = true
    }

    @CallSuper
    override fun onMenuItemSelected(id: Int): Boolean {
        return when (id) {
            android.R.id.home -> {
                viewModel.onBackPressed()
                true
            }
            R.id.edit -> {
                viewModel.onEditClicked()
                true
            }
            R.id.delete -> {
                onDeleteSelected()
                true
            }
            else -> false
        }
    }

    open fun onSwitchToEditMode() {}

    private fun switchToMode(mode: DetailsViewModel.Mode) = binding?.run {
        when (mode) {
            DetailsViewModel.Mode.View -> switchToViewMode()
            DetailsViewModel.Mode.Edit -> switchToEditMode()
        }
    }

    private fun T.switchToEditMode() = input.run {
        onSwitchToEditMode()
        this.makeEditable()

        lifecycleScope.launchWhenResumed {
            delay(100)
            runCatching { menu?.getItem(0)?.setTitle(R.string.save) }
        }

        val duration = transitionDuration
        content.animate()
            .alpha(0f)
            .translationY(30.dp.toPx(requireContext()).toFloat())
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            content.isGone = true
            showKeyboard()
            goalInput.isVisible = true
            goalInput.alpha = 0f
            goalInput.translationY = 0f
            goalInput.animate()
                .setDuration(transitionDuration)
                .translationY(-30.dp.toPx(requireContext()).toFloat())
                .alpha(1f)
                .start()
        }

        saveBtn.isGone = false
        saveBtn.alpha = 0f

        saveBtn.animate()
            .alpha(1f)
            .setDuration(duration)
            .start()
    }

    private fun T.switchToViewMode() = input.run {
        this.makeNonEditable()

        menu?.getItem(0)?.setTitle(R.string.edit)
        val duration = shortTransitionDuration

        content.isGone = false

        content.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(duration)
            .start()

        saveBtn.animate()
            .alpha(0f)
            .setDuration(duration)
            .start()

        lifecycleScope.launch {
            delay(duration)
            saveBtn.isGone = true
            hideKeyboard()
        }

        goalInput.isVisible = false
    }

    private fun EditText.makeEditable() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setSelection(text.length)
    }

    private fun EditText.makeNonEditable() {
        inputType = EditorInfo.TYPE_NULL

        isFocusable = false
        isFocusableInTouchMode = false
        setSelection(0)
        clearFocus()
    }

    private val transitionDuration
        get() = resources.getInteger(R.integer.animation_duration).toLong()

    private val shortTransitionDuration
        get() = resources.getInteger(R.integer.animation_duration_short).toLong()
}
