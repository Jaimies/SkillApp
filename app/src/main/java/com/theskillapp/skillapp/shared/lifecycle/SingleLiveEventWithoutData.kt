package com.theskillapp.skillapp.shared.lifecycle

class SingleLiveEventWithoutData : SingleLiveEvent<Unit>() {
    fun call() {
        value = Unit
    }
}
