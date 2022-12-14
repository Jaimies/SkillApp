package com.maxpoliakov.skillapp.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object LocalDateAsStringSerializer : KSerializer<LocalDate> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun serialize(encoder: Encoder, value: LocalDate) {
        val string = value.format(formatter)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalDate {
        val string = decoder.decodeString()
        return formatter.parse(string, LocalDate::from)
    }

    override val descriptor = PrimitiveSerialDescriptor("LocalDateAsStringDescriptor", PrimitiveKind.STRING)
}

object LocalTimeAsStringSerializer: KSerializer<LocalTime> {
    private val formatter = DateTimeFormatter.ISO_LOCAL_TIME

    override fun serialize(encoder: Encoder, value: LocalTime) {
        val string = value.format(formatter)
        encoder.encodeString(string)
    }

    override fun deserialize(decoder: Decoder): LocalTime {
        val string = decoder.decodeString()
        return formatter.parse(string, LocalTime::from)
    }

    override val descriptor = PrimitiveSerialDescriptor("LocalTimeAsStringDescriptor", PrimitiveKind.STRING)
}
