package com.jdevs.timeo.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

fun <T> createLiveData() = MutableLiveData<T>() as LiveData<T>
