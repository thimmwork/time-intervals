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
import net.thimmwork.time.interval.LocalDateInterval
import java.io.IOException
import java.time.LocalDate
import java.util.*

class LocalDateIntervalDeserializer : JsonDeserializer<LocalDateInterval>() {
    @Throws(IOException::class, JsonProcessingException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateInterval {
        val map: Map<String, String> = p.codec.readValue(p, object : TypeReference<HashMap<String, String>>() {})
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
