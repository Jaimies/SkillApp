package com.maxpoliakov.skillapp.data.serialization

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.PairSerializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.LocalTime

object ClosedLocalTimeRangeAsPairSerializer : KSerializer<ClosedRange<LocalTime>> {
    private val serializer = PairSerializer(LocalTimeAsStringSerializer, LocalTimeAsStringSerializer)
    override val descriptor = serializer.descriptor

    override fun serialize(encoder: Encoder, value: ClosedRange<LocalTime>) {
        encoder.encodeSerializableValue(
            serializer,
            Pair(value.start, value.endInclusive),
        )
    }

    override fun deserialize(decoder: Decoder): ClosedRange<LocalTime> {
        val pair = decoder.decodeSerializableValue(serializer)
        return pair.first..pair.second
    }
}
