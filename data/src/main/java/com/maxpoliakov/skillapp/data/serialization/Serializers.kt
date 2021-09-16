package com.maxpoliakov.skillapp.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Duration
import java.time.LocalDate
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

object DurationAsLongSerializer : KSerializer<Duration> {
    override fun serialize(encoder: Encoder, value: Duration) {
        encoder.encodeLong(value.toMillis())
    }

    override fun deserialize(decoder: Decoder): Duration {
        return Duration.ofMillis(decoder.decodeLong())
    }

    override val descriptor = PrimitiveSerialDescriptor("DurationAsLongSerializer", PrimitiveKind.LONG)
}