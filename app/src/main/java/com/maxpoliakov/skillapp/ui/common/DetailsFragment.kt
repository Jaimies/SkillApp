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

    @CallSuper
    override fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
        super.onBindingCreated(binding, savedInstanceState)

        binding.run {
            saveBtn.isGone = true
            input.isSingleLine = true
            input.maxLines = 2
            input.setHorizontallyScrolling(false)
            input.exitEditMode()
        }

        if (viewModel.isEditing.value!!)
            binding.startEditing()

        observe(viewModel.onSave) { this.binding?.stopEditing() }

        observe(viewModel.chooseGoal) {
            viewModel.unit.value!!.showGoalPicker(
                childFragmentManager,
                viewModel.goal.value?.mapToDomain(),
                onGoalSet = viewModel::setGoal
            )
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            isEnabled = !onBackPressed()
        }
    }

    private fun onBackPressed(): Boolean {
        if (viewModel.isEditing.value!!) {
            viewModel.exitEditingMode()
            binding?.stopEditing()
            return false
        } else {
            findNavController().navigateUp()
            return true
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
        return when(id) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.edit -> {
                onEditClicked()
                true
            }
            R.id.delete -> {
                onDeleteSelected()
                true
            }
            else -> false
        }
    }

    private fun onEditClicked() {
        if (viewModel.isEditing.value!!)
            viewModel.save()
        else
            binding?.startEditing()
    }

    open fun onStartEditing() {}

    protected open fun T.startEditing() = input.run {
        onStartEditing()
        enterEditMode()

        viewModel.enterEditingMode()

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

    protected open fun T.stopEditing() = input.run {
        exitEditMode()

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

    private fun EditText.enterEditMode() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES

        isFocusable = true
        isFocusableInTouchMode = true
        requestFocus()
        setSelection(text.length)
    }

    private fun EditText.exitEditMode() {
        inputType = EditorInfo.TYPE_NULL

        isFocusable = false
        isFocusableInTouchMode = false
        setSelection(0)
        clearFocus()
    }

    protected val transitionDuration
        get() = resources.getInteger(R.integer.animation_duration).toLong()

    protected val shortTransitionDuration
        get() = resources.getInteger(R.integer.animation_duration_short).toLong()
}
