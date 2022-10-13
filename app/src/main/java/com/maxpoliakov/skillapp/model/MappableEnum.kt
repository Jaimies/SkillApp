package com.maxpoliakov.skillapp.model

interface MappableEnum<Type, DomainType> {
    fun toDomain(): DomainType

    abstract class Companion<Type : MappableEnum<Type, DomainType>, DomainType>(private val enumValues: Array<Type>) {
        fun from(unit: DomainType): Type {
            return enumValues.find { value -> value.toDomain() == unit }
                ?: throw IllegalArgumentException("Unknown enum value: $unit")
        }

        fun DomainType.mapToUI() = from(this)
    }
}
