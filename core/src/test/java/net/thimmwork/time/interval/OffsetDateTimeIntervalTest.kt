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
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OffsetDateTimeIntervalTest {
    val year2018withCEToffset = OffsetDateTimeInterval(parse("2018-01-01T00:00:00+01:00"), parse("2019-01-01T00:00:00+01:00"))

    @Test
    fun `Infiniy contains the year 2018 CET`() {
        val infinity = Infinity.INSTANT_INTERVAL

        assertTrue { infinity.contains(year2018withCEToffset.toInstantInterval()) }
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = OffsetDateTimeInterval(parse("1900-01-01T00:00:00+01:00"), parse("2000-01-01T00:00:00+01:00"))
        val normalized = twentiethCentury.normalize()

        val expected = OffsetDateTimeInterval(parse("1970-01-01T01:00:00+01:00"), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 5k is normalized to dec 31th 4000`() {
        val hugeInterval = OffsetDateTimeInterval(parse("2000-01-01T00:00:00+01:00"), parse("5000-01-01T00:00:00+01:00"))
        val normalized = hugeInterval.normalize()

        val expected = OffsetDateTimeInterval(hugeInterval.start, parse("4000-12-31T01:00:00+01:00"))
        assertEquals(expected, normalized)
    }

    @Test
    fun `2018 contains first and last milli of the year, but not first milli of 2019`() {
        assertTrue { year2018withCEToffset.contains(parse("2018-01-01T00:00:00.000+01:00")) }
        assertTrue { year2018withCEToffset.contains(parse("2018-12-31T23:59:59.999+01:00")) }
        assertFalse { year2018withCEToffset.contains(parse("2019-01-01T00:00:00.000+01:00")) }
    }

    @Test
    fun `zero-length OffsetDateTimeInterval with start=end does not contain start`() {
        val dateTime = OffsetDateTime.parse("2018-01-01T00:00:00.000+01:00")
        val interval = OffsetDateTimeInterval(dateTime, dateTime)
        assertFalse { interval.contains(dateTime.minusSeconds(1)) }
        assertFalse { interval.contains(dateTime) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempt to create interval with end before start will throw IllegalArgumentException`() {
        OffsetDateTimeInterval(parse("2018-12-31T00:00:00.000+01:00"), parse("2018-01-01T00:00:00.000+01:00"))
    }

    @Test
    fun `conversion to range creates a closed-open range`() {
        val range2018 = year2018withCEToffset.toRange()
        assertTrue { range2018.hasLowerBound() }
        assertTrue { range2018.lowerBoundType() == BoundType.CLOSED }
        assertTrue { range2018.lowerEndpoint() == year2018withCEToffset.start }
        assertTrue { range2018.hasUpperBound() }
        assertTrue { range2018.upperBoundType() == BoundType.OPEN }
        assertTrue { range2018.upperEndpoint() == year2018withCEToffset.end }
    }

    @Test
    fun `two intervals with same bounds have the same hashCode`() {
        val otherInterval = OffsetDateTimeInterval(year2018withCEToffset.start, year2018withCEToffset.end)

        assertTrue { year2018withCEToffset.hashCode() == otherInterval.hashCode() }
    }

    @Test
    fun `two intervals with same bounds are equal`() {
        val otherInterval = OffsetDateTimeInterval(year2018withCEToffset.start, year2018withCEToffset.end)
        val differentInterval = OffsetDateTimeInterval(year2018withCEToffset.start, year2018withCEToffset.end.plusSeconds(1))

        assertTrue { year2018withCEToffset == otherInterval }
        assertFalse { year2018withCEToffset == differentInterval }
        assertFalse { otherInterval == differentInterval }
    }

    @Test(expected = java.lang.IllegalArgumentException::class)
    fun `gap throws IllegalArgumentException on overlapping intervals`() {
        val overlappingInterval2018_19 = OffsetDateTimeInterval.parse("2018-12-01T00:00:00+01:00", "2020-01-01T00:00:00+01:00")

        year2018withCEToffset.gap(overlappingInterval2018_19)
    }

    @Test
    fun `gap returns gap on succeeding interval`() {
        val jan2nd2019 = OffsetDateTimeInterval.parse("2019-01-02T00:00:00+01:00", "2019-01-02T00:00:00+01:00")

        assertTrue { year2018withCEToffset.gap(jan2nd2019) == OffsetDateTimeInterval.parse("2019-01-01T00:00:00+01:00", "2019-01-02T00:00:00+01:00") }
    }

    @Test
    fun parse() {
        val begin = "2018-02-01T03:04:05+06:30"
        val end = "2019-05-06T07:08:09+10:45"
        val interval = OffsetDateTimeInterval(OffsetDateTime.parse(begin), parse(end))
        val interval2 = OffsetDateTimeInterval.parse(begin, end)

        assertTrue { interval == interval2 }
    }

    class OverlapsTests {
        val t1 = OffsetDateTime.parse("2000-01-01T00:00:00Z")
        val t2 = OffsetDateTime.parse("2000-02-01T00:00:00Z")
        val t3 = OffsetDateTime.parse("2000-03-01T00:00:00Z")
        val t4 = OffsetDateTime.parse("2000-04-01T00:00:00Z")

        @Test
        fun `case1 - x starts before y and overlaps`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t2, t4)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case2 - y starts before x and overlaps`() {
            val x = OffsetDateTimeInterval(t2, t4)
            val y = OffsetDateTimeInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case3 - x and y touch at start of x`() {
            val x = OffsetDateTimeInterval(t3, t4)
            val y = OffsetDateTimeInterval(t2, t3)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case4 - x and y touch at end of x`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t2, t3)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case5 - x starts after y and both have the same end`() {
            val x = OffsetDateTimeInterval(t2, t3)
            val y = OffsetDateTimeInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case6 - x ends before y and both have the same start`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case7 - x ends after y and both have the same start`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t1, t2)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case8 - x starts before y and both have the same end`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case9 - x contains y`() {
            val x = OffsetDateTimeInterval(t1, t4)
            val y = OffsetDateTimeInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case10 - y contains x`() {
            val x = OffsetDateTimeInterval(t2, t3)
            val y = OffsetDateTimeInterval(t1, t4)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case11 - x is after y`() {
            val x = OffsetDateTimeInterval(t3, t4)
            val y = OffsetDateTimeInterval(t1, t2)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case12 - y is after x`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t3, t4)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case13 - x equals y`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t1, t2)

            assertTrue { x overlaps y }
        }
    }

    class OverlapTests {
        val t1 = OffsetDateTime.parse("2000-01-01T00:00:00Z")
        val t2 = OffsetDateTime.parse("2000-02-01T00:00:00Z")
        val t3 = OffsetDateTime.parse("2000-03-01T00:00:00Z")
        val t4 = OffsetDateTime.parse("2000-04-01T00:00:00Z")

        @Test
        fun `case1 - x starts before y and overlaps`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t2, t4)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case2 - y starts before x and overlaps`() {
            val x = OffsetDateTimeInterval(t2, t4)
            val y = OffsetDateTimeInterval(t1, t3)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case3 - x and y touch at start of x`() {
            val x = OffsetDateTimeInterval(t3, t4)
            val y = OffsetDateTimeInterval(t2, t3)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case4 - x and y touch at end of x`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t2, t3)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case5 - x starts after y and both have the same end`() {
            val x = OffsetDateTimeInterval(t2, t3)
            val y = OffsetDateTimeInterval(t1, t3)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case6 - x ends before y and both have the same start`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t1, t3)

            assertEquals( OffsetDateTimeInterval(t1, t2), x overlap y )
        }

        @Test
        fun `case7 - x ends after y and both have the same start`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t1, t2)

            assertEquals( OffsetDateTimeInterval(t1, t2), x overlap y )
        }

        @Test
        fun `case8 - x starts before y and both have the same end`() {
            val x = OffsetDateTimeInterval(t1, t3)
            val y = OffsetDateTimeInterval(t2, t3)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case9 - x contains y`() {
            val x = OffsetDateTimeInterval(t1, t4)
            val y = OffsetDateTimeInterval(t2, t3)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case10 - y contains x`() {
            val x = OffsetDateTimeInterval(t2, t3)
            val y = OffsetDateTimeInterval(t1, t4)

            assertEquals( OffsetDateTimeInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case11 - x is after y`() {
            val x = OffsetDateTimeInterval(t3, t4)
            val y = OffsetDateTimeInterval(t1, t2)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case12 - y is after x`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t3, t4)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case13 - x equals y`() {
            val x = OffsetDateTimeInterval(t1, t2)
            val y = OffsetDateTimeInterval(t1, t2)

            assertEquals( OffsetDateTimeInterval(t1, t2), x overlap y )
        }
    }
}