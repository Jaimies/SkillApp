package com.jdevs.timeo.lifecycle

import androidx.lifecycle.MutableLiveData

/**
 * [MutableLiveData] that only accepts new values in [setValue] method
 *
 * @param T type of data hold by this instance
 */
class SmartLiveData<T> : MutableLiveData<T> {

    private constructor() : super()
    private constructor(value: T) : super(value)

    override fun setValue(value: T) {
        if (this.value != value) super.setValue(value)
    }

    /**
     * Static constructors that expose new instances as [MutableLiveData]
     */
    companion object {

        /**
         * Creates a SmartLiveData with no value assigned to it
         */
        operator fun <T> invoke(): MutableLiveData<T> = SmartLiveData()

        /**
         * Creates a SmartLiveData initialized with the given [value]
         */
        operator fun <T> invoke(value: T): MutableLiveData<T> = SmartLiveData(value)
    }
}
