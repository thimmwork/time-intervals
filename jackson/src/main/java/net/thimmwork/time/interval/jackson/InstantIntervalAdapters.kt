package net.thimmwork.time.interval.jackson

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import net.thimmwork.time.constant.Infinity
import net.thimmwork.time.interval.InstantInterval
import java.io.IOException
import java.time.Instant
import java.util.*

class InstantIntervalDeserializer : JsonDeserializer<InstantInterval>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): InstantInterval {
        val map = p.codec.readValue<Map<String, String>>(p, object : TypeReference<HashMap<String, String>>() {})
        val start = map["start"]?.toInstant() ?: Infinity.INSTANT_INTERVAL.start
        val end = map["end"]?.toInstant() ?: Infinity.INSTANT_INTERVAL.end
        return InstantInterval(start, end)
    }
}

class InstantIntervalSerializer : JsonSerializer<InstantInterval>() {
    override fun handledType() = InstantInterval::class.java

    override fun serialize(value: InstantInterval?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null || value == Infinity.INSTANT_INTERVAL) {
            gen.writeNull()
        } else {
            gen.apply {
                writeStartObject()
                writeStringField("start", value.start.toString())
                writeStringField("end", value.end.toString())
            }
        }
    }
}

fun String?.toInstant(): Instant? = this?.let { Instant.parse(this) }
