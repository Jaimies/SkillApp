package com.jdevs.timeo.domain.model

sealed class Operation<out T> {
    class LastItemReached<T : Any> : Operation<T>()
    class Successful<T : Any> : Operation<T>()

    class Changed<T : Any>(
        val item: T,
        val changeType: ChangeType,
        val newIndex: Int
    ) : Operation<T>()

    enum class ChangeType {
        Added, Modified, Removed
    }
}
