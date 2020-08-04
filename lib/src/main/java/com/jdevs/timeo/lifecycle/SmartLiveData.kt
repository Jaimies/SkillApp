package com.jdevs.timeo.lifecycle

import androidx.lifecycle.MutableLiveData

/**
 * [MutableLiveData] that only notifies observers about new values
 * when [setValue] method is called
 */
class SmartLiveData<T> : MutableLiveData<T> {
    constructor() : super()
    constructor(value: T) : super(value)

    override fun setValue(value: T?) {
        if (this.value != value) super.setValue(value)
    }
}
