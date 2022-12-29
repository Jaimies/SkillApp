package com.maxpoliakov.skillapp.ui.common

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class DataBindingFragment<T : ViewDataBinding> : BaseFragment() {
    abstract val layoutId: Int

    private var _binding: T? = null
    protected val binding: T
        get() = requireNotNull(_binding) {
            "Trying to access binding before onCreateView() is called or after onDestroyView() is called"
        }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onBindingCreated(this)
        }

        return binding.root
    }

    protected open fun onBindingCreated(binding: T) {
    }

    protected open fun clearViewReferences() {
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        clearViewReferences()
        _binding = null
    }
}
