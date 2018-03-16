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
import net.thimmwork.time.interval.LocalDateInterval
import java.io.IOException
import java.time.LocalDate
import java.util.*

class LocalDateIntervalDeserializer : JsonDeserializer<LocalDateInterval>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateInterval {
        val map = p.codec.readValue<Map<String, String>>(p, object : TypeReference<HashMap<String, String>>() {})
        val start = map["start"]?.toLocalDate() ?: Infinity.LOCAL_DATE_INTERVAL.start
        val end = map["end"]?.toLocalDate() ?: Infinity.LOCAL_DATE_INTERVAL.end
        return LocalDateInterval(start, end)
    }
}

class LocalDateIntervalSerializer : JsonSerializer<LocalDateInterval>() {
    override fun handledType() = LocalDateInterval::class.java

    override fun serialize(value: LocalDateInterval?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null || value == Infinity.LOCAL_DATE_INTERVAL) {
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

fun String?.toLocalDate(): LocalDate? = this?.let { LocalDate.parse(this) }
