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
import net.thimmwork.time.interval.InstantInterval
import net.thimmwork.time.interval.instantInterval
import org.junit.Assert.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.ZoneId

class InstantIntervalJacksonTest {

    private val objectMapper = ObjectMapper()

    @BeforeEach
    fun setUp() {
        objectMapper.registerModule(MODULE)
    }

    @org.junit.jupiter.api.Test
    fun `deserialize JSON to InstantInterval`() {
        val actualInterval = objectMapper.readValue(
                "{" +
                        " \"start\": \"2018-01-01T00:00:00Z\"," +
                        " \"end\": \"2019-01-01T00:00:00Z\" " +
                        "}",
                InstantInterval::class.java)

        val expected = instantInterval("2018-01-01T00:00", "2019-01-01T00:00", ZoneId.of("UTC"))
        assertEquals(expected, actualInterval)
    }

    @Test
    fun `serialize InstantInterval to JSON`() {
        val instantInterval = instantInterval("2018-01-01T00:00", "2019-01-01T00:00", ZoneId.of("UTC"))
        val actualJson = objectMapper.writeValueAsString(instantInterval)

        val expected = "{\"start\":\"2018-01-01T00:00:00Z\",\"end\":\"2019-01-01T00:00:00Z\"}"
        assertEquals(expected, actualJson)
    }
}
