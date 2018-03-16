package net.thimmwork.time.interval.jackson

import com.fasterxml.jackson.databind.ObjectMapper
import net.thimmwork.time.interval.LocalDateInterval
import net.thimmwork.time.interval.localDateInterval
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
        val actualInterval = objectMapper.readValue("{ \"start\": \"2018-01-01\", \"end\": \"2018-12-31\" }", LocalDateInterval::class.java)

        val expected = localDateInterval("2018-01-01", "2018-12-31")
        assertEquals(expected, actualInterval)
    }

    @Test
    fun `serialize LocalDateInterval to JSON`() {
        val localDateInterval = localDateInterval("2018-01-01", "2018-12-31")
        val actualJson = objectMapper.writeValueAsString(localDateInterval)

        val expected = "{\"start\":\"2018-01-01\",\"end\":\"2018-12-31\"}"
        assertEquals(expected, actualJson)
    }
}
