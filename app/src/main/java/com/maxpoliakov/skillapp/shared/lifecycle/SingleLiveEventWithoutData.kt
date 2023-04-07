package com.maxpoliakov.skillapp.shared.lifecycle

class SingleLiveEventWithoutData : SingleLiveEvent<Unit>() {
    fun call() {
        value = Unit
    }
}
