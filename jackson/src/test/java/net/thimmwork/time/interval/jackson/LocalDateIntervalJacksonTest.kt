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
import net.thimmwork.time.interval.LocalDateInterval
import net.thimmwork.time.interval.LocalDateInterval.Companion.parse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalDateIntervalJacksonTest {

    private val objectMapper = ObjectMapper()

    @Before
    fun setUp() {
        objectMapper.registerModule(MODULE)
    }

    @Test
    fun `deserialize JSON to LocalDateInterval`() {
        val actualInterval = objectMapper.readValue(
                "{" +
                        " \"start\": \"2018-01-01\"," +
                        " \"end\": \"2018-12-31\"" +
                        "}",
                LocalDateInterval::class.java)

        val expected = parse("2018-01-01", "2018-12-31")
        assertEquals(expected, actualInterval)
    }

    @Test
    fun `serialize LocalDateInterval to JSON`() {
        val localDateInterval = parse("2018-01-01", "2018-12-31")
        val actualJson = objectMapper.writeValueAsString(localDateInterval)

        val expected = "{\"start\":\"2018-01-01\",\"end\":\"2018-12-31\"}"
        assertEquals(expected, actualJson)
    }
}
