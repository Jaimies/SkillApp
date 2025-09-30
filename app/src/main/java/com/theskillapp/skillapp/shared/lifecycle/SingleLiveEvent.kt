package com.theskillapp.skillapp.shared.lifecycle

import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A MutableLiveData, that makes sure that the observer is only notified about the new value
 * once (i.e. resubscribing won't trigger the observer).
 *
 * In case there are multiple observers, only one is going to be notified of changes,
 * and it is not certain which one.
 *
 * If null is passed to setValue, the call will be ignored. This is done in
 * an effort to compensate for lack of null-safety in LiveData.
 */
open class SingleLiveEvent<T : Any> : MutableLiveData<T>() {
    private val pending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            Log.w(TAG, "Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner) { value ->
            if (pending.compareAndSet(true, false)) {
                observer.onChanged(value)
            }
        }
    }

    @MainThread
    override fun setValue(value: T?) {
        if (value == null) return
        pending.set(true)
        super.setValue(value)
    }

    companion object {
        private const val TAG = "SingleLiveEvent"
    }
}
