package com.theskillapp.skillapp.shared

interface MappableEnum<Type, DomainType> {
    val domainCounterpart: DomainType

    abstract class Companion<Type : MappableEnum<Type, DomainType>, DomainType>(private val enumValues: Array<out Type>) {
        fun from(unit: DomainType): Type {
            return enumValues.find { value -> value.domainCounterpart == unit }
                ?: throw IllegalArgumentException("Unknown enum value: $unit")
        }

        fun DomainType.mapToUI() = from(this)
    }
}
