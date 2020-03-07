package com.jdevs.timeo.lifecycle

import androidx.lifecycle.MutableLiveData

/**
 * [MutableLiveData] that only accepts new values in [setValue] method
 *
 * @param T type of data hold by this instance
 */
class SmartLiveData<T> : MutableLiveData<T>() {

    override fun setValue(value: T?) {
        if (this.value != value) super.setValue(value)
    }
}
