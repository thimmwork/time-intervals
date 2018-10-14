/**
 * Copyright 2018 thimmwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import net.thimmwork.time.interval.OffsetDateTimeInterval
import java.io.IOException
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

class OffsetDateTimeIntervalDeserializer : JsonDeserializer<OffsetDateTimeInterval>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OffsetDateTimeInterval {
        val map = p.codec.readValue<Map<String, String>>(p, object : TypeReference<HashMap<String, String>>() {})
        val start = map["start"]?.toOffsetDateTime() ?: Infinity.INSTANT_INTERVAL.start.atOffset(ZoneOffset.UTC)
        val end = map["end"]?.toOffsetDateTime() ?: Infinity.INSTANT_INTERVAL.end.atOffset(ZoneOffset.UTC)
        return OffsetDateTimeInterval(start, end)
    }
}

class OffsetDateTimeIntervalSerializer : JsonSerializer<OffsetDateTimeInterval>() {
    override fun handledType() = OffsetDateTimeInterval::class.java

    override fun serialize(value: OffsetDateTimeInterval?, gen: JsonGenerator, serializers: SerializerProvider?) {
        if (value == null) {
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

fun String?.toOffsetDateTime(): OffsetDateTime? = this?.let { OffsetDateTime.parse(this) }
