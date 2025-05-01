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

import com.fasterxml.jackson.databind.ObjectMapper
import net.thimmwork.time.interval.OffsetDateTimeInterval
import net.thimmwork.time.interval.OffsetDateTimeInterval.Companion.parse
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class OffsetDateTimeIntervalJacksonTest {

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        objectMapper.registerModule(MODULE)
    }

    @Test
    fun `deserialize JSON to OffsetDateTimeInterval`() {
        val actualInterval = objectMapper.readValue(
                "{" +
                        " \"start\": \"2018-01-01T00:00:00+01:00\"," +
                        " \"end\": \"2019-01-01T00:00:00+01:00\" " +
                        "}",
                OffsetDateTimeInterval::class.java)

        val expected = parse("2018-01-01T00:00:00+01:00", "2019-01-01T00:00:00+01:00")
        assertEquals(expected, actualInterval)
    }

    @org.junit.jupiter.api.Test
    fun `serialize OffsetDateTimeInterval to JSON`() {
        val instantInterval = parse("2018-01-01T00:00:00+01:00", "2019-01-01T00:00:00+01:00")
        val actualJson = objectMapper.writeValueAsString(instantInterval)

        val expected = "{\"start\":\"2018-01-01T00:00+01:00\",\"end\":\"2019-01-01T00:00+01:00\"}"
        assertEquals(expected, actualJson)
    }
}
