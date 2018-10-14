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

package net.thimmwork.time.interval

import com.google.common.collect.BoundType
import net.thimmwork.time.constant.Infinity
import org.junit.Test
import java.time.LocalDateTime
import java.time.LocalDateTime.parse
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateTimeIntervalTest {
    val year2018 = LocalDateTimeInterval(parse("2018-01-01T00:00:00"), parse("2019-01-01T00:00:00"))

    @Test
    fun `Infiniy contains the year 2018`() {
        val infinity = Infinity.LOCAL_DATE_TIME_INTERVAL

        assertTrue { infinity.contains(year2018) }
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = LocalDateTimeInterval(parse("1900-01-01T00:00:00"), parse("2000-01-01T00:00:00"))
        val normalized = twentiethCentury.normalize()

        val expected = LocalDateTimeInterval(parse("1970-01-01T00:00:00"), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 5k is normalized to dec 31th 4000`() {
        val hugeInterval = LocalDateTimeInterval(parse("2000-01-01T00:00:00"), parse("5000-01-01T00:00:00"))
        val normalized = hugeInterval.normalize()

        val expected = LocalDateTimeInterval(hugeInterval.start, parse("4000-12-31T00:00:00"))
        assertEquals(expected, normalized)
    }

    @Test
    fun `2018 contains first and last milli of the year, but not first milli of 2019`() {
        assertTrue { year2018.contains(parse("2018-01-01T00:00:00.000")) }
        assertTrue { year2018.contains(parse("2018-12-31T23:59:59.999")) }
        assertFalse { year2018.contains(parse("2019-01-01T00:00:00.000")) }
    }

    @Test
    fun `zero-length LocalDateTimeInterval with start=end does not contain start`() {
        val dateTime = LocalDateTime.parse("2018-01-01T00:00:00.000")
        val interval = LocalDateTimeInterval(dateTime, dateTime)
        assertFalse { interval.contains(dateTime.minusSeconds(1)) }
        assertFalse { interval.contains(dateTime) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempt to create interval with end before start will throw IllegalArgumentException`() {
        LocalDateTimeInterval(parse("2018-12-31T00:00:00.000"), parse("2018-01-01T00:00:00.000"))
    }

    @Test
    fun `conversion to range creates a closed-open range`() {
        val range2018 = year2018.toRange()
        assertTrue { range2018.hasLowerBound() }
        assertTrue { range2018.lowerBoundType() == BoundType.CLOSED }
        assertTrue { range2018.lowerEndpoint() == year2018.start }
        assertTrue { range2018.hasUpperBound() }
        assertTrue { range2018.upperBoundType() == BoundType.OPEN }
        assertTrue { range2018.upperEndpoint() == year2018.end }
    }

    @Test
    fun `two intervals with same bounds have the same hashCode`() {
        val otherInterval = LocalDateTimeInterval(year2018.start, year2018.end)

        assertTrue { year2018.hashCode() == otherInterval.hashCode() }
    }

    @Test
    fun `two intervals with same bounds are equal`() {
        val otherInterval = LocalDateTimeInterval(year2018.start, year2018.end)
        val differentInterval = LocalDateTimeInterval(year2018.start, year2018.end.plusSeconds(1))

        assertTrue { year2018 == otherInterval }
        assertFalse { year2018 == differentInterval }
        assertFalse { otherInterval == differentInterval }
    }
}