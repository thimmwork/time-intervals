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
import java.time.OffsetDateTime
import java.time.OffsetDateTime.parse
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InstantIntervalTest {
    val interval2018utc = instantInterval("2018-01-01T00:00:00", "2019-01-01T00:00:00", ZoneId.of("UTC"))

    @Test
    fun `InstantInterval uses a human-readable toString()`() {
        val interval = instantInterval("2018-02-01T12:43:59.999", "2019-03-04T17:39:51.888", ZoneId.of("UTC"))

        assertEquals("Interval(start=2018-02-01T12:43:59.999Z,end=2019-03-04T17:39:51.888Z)", interval.toString())
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = InstantInterval(parse("1900-01-01T00:00Z").toInstant(), parse("1999-12-31T00:00Z").toInstant())
        val normalized = twentiethCentury.normalize()

        val expected = InstantInterval(parse("1970-01-01T00:00Z").toInstant(), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 10k is normalized to dec 31th 4000`() {
        val hugeInterval = InstantInterval(parse("2000-01-01T00:00Z").toInstant(), parse("5000-01-01T00:00Z").toInstant())
        val normalized = hugeInterval.normalize()

        val expected = InstantInterval(hugeInterval.start, parse("4000-12-31T00:00Z").toInstant())
        assertEquals(expected, normalized)
    }

    @Test
    fun `test hashCode() when an interval is added to a Set multiple times, the set contains only one element`() {
        val set = HashSet<InstantInterval>()

        set.add(interval2018utc)
        set.add(instantInterval("2018-01-01T00:00:00", "2019-01-01T00:00:00", ZoneId.of("UTC")))

        assertEquals(1, set.size)
    }

    @Test
    fun `Infiniy contains the year 2018 UTC`() {
        val infinity = Infinity.INSTANT_INTERVAL

        assertTrue { infinity.contains(interval2018utc) }
    }

    @Test
    fun `2018 contains first and last milli of the year, but not first milli of 2019`() {
        assertTrue { interval2018utc.contains(OffsetDateTime.parse("2018-01-01T00:00:00.000Z").toInstant()) }
        assertTrue { interval2018utc.contains(OffsetDateTime.parse("2018-12-31T23:59:59.999Z").toInstant()) }
        assertFalse { interval2018utc.contains(OffsetDateTime.parse("2019-01-01T00:00:00.000Z").toInstant()) }
    }

    @Test
    fun `conversion to range creates a closed-open range`() {
        val range2018 = interval2018utc.toRange()
        assertTrue { range2018.hasLowerBound() }
        assertTrue { range2018.lowerBoundType() == BoundType.CLOSED }
        assertTrue { range2018.lowerEndpoint() == parse("2018-01-01T00:00Z").toInstant() }
        assertTrue { range2018.hasUpperBound() }
        assertTrue { range2018.upperBoundType() == BoundType.OPEN }
        assertTrue { range2018.upperEndpoint() == parse("2019-01-01T00:00Z").toInstant() }
    }

    @Test
    fun `two intervals with same bounds have the same hashCode`() {
        val otherInterval = InstantInterval(interval2018utc.start, interval2018utc.end)

        assertTrue { interval2018utc.hashCode() == otherInterval.hashCode() }
    }

    @Test
    fun `two intervals with same bounds are equal`() {
        val otherInterval = InstantInterval(interval2018utc.start, interval2018utc.end)
        val differentInterval = InstantInterval(interval2018utc.start, interval2018utc.end.plusMillis(1))

        assertTrue { interval2018utc == otherInterval }
        assertFalse { interval2018utc == differentInterval }
        assertFalse { otherInterval == differentInterval }
    }
}