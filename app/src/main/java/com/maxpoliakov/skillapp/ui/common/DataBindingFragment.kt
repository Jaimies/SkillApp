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

    protected var binding: T? = null
        private set

    protected fun requireBinding(): T = requireNotNull(binding) {
        "Trying to access binding before onCreateView() is called or after onDestroyView() is called"
    }

    @CallSuper
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<T>(inflater, layoutId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            onBindingCreated(this, savedInstanceState)
        }

        this.binding = binding
        return binding.root
    }

    @CallSuper
    protected open fun onBindingCreated(binding: T, savedInstanceState: Bundle?) {
    }

    @CallSuper
    protected open fun onPreDestroyBinding(binding: T) {
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        onPreDestroyBinding(binding!!)
        binding = null
    }
}
