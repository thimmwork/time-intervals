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
import java.time.LocalDate
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateIntervalTest {
    private val interval2018 = LocalDateInterval.parse("2018-01-01", "2018-12-31")

    @Test
    fun `Infiniy contains the year 2018`() {
        val infinity = Infinity.LOCAL_DATE_INTERVAL

        assertTrue { infinity.contains(interval2018) }
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = LocalDateInterval.parse("1900-01-01", "1999-12-31")
        val normalized = twentiethCentury.normalize()

        val expected = LocalDateInterval(LocalDate.parse("1970-01-01"), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 5k is normalized to dec 31th 4000`() {
        val hugeInterval = LocalDateInterval.parse("2000-01-01", "5000-01-01")
        val normalized = hugeInterval.normalize()

        val expected = LocalDateInterval(hugeInterval.start, LocalDate.parse("4000-12-31"))
        assertEquals(expected, normalized)
    }

    @Test
    fun `2018 contains first and last day of the year, but not first day of 2019`() {
        assertTrue { interval2018.contains(LocalDate.parse("2018-01-01")) }
        assertTrue { interval2018.contains(LocalDate.parse("2018-12-31")) }
        assertFalse { interval2018.contains(LocalDate.parse("2019-01-01")) }
    }

    @Test
    fun `LocalDateInterval with start=end contains exactly that date`() {
        val date = LocalDate.parse("2018-01-01")
        val interval = LocalDateInterval(date, date)
        assertTrue { interval.contains(date) }
        assertFalse { interval.contains(date.minusDays(1)) }
        assertFalse { interval.contains(date.plusDays(1)) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempt to create interval with end before start will throw IllegalArgumentException`() {
        LocalDateInterval.parse("2018-12-31", "2018-01-01")
    }

    @Test
    fun `conversion to range creates a closed range`() {
        val range2018 = interval2018.toRange()
        assertTrue { range2018.hasLowerBound() }
        assertTrue { range2018.lowerBoundType() == BoundType.CLOSED }
        assertTrue { range2018.lowerEndpoint() == LocalDate.parse("2018-01-01") }
        assertTrue { range2018.hasUpperBound() }
        assertTrue { range2018.upperBoundType() == BoundType.CLOSED }
        assertTrue { range2018.upperEndpoint() == LocalDate.parse("2018-12-31") }
    }

    @Test
    fun `two intervals with same bounds have the same hashCode`() {
        val otherInterval = LocalDateInterval(interval2018.start, interval2018.end)

        assertTrue { interval2018.hashCode() == otherInterval.hashCode() }
    }

    @Test
    fun `two intervals with same bounds are equal`() {
        val otherInterval = LocalDateInterval(interval2018.start, interval2018.end)
        val differentInterval = LocalDateInterval(interval2018.start, interval2018.end.plusDays(1))

        assertTrue { interval2018 == otherInterval }
        assertFalse { interval2018 == differentInterval }
        assertFalse { otherInterval == differentInterval }
    }

    @Test
    fun parse() {
        val begin = "2018-02-01"
        val end = "2019-05-06"
        val interval = LocalDateInterval(LocalDate.parse(begin), LocalDate.parse(end))
        val interval2 = LocalDateInterval.parse(begin, end)

        assertTrue { interval == interval2 }
    }

    class OverlapsTests {
        val t1 = LocalDate.parse("2000-01-01")
        val t2 = LocalDate.parse("2000-02-01")
        val t3 = LocalDate.parse("2000-03-01")
        val t4 = LocalDate.parse("2000-04-01")

        @Test
        fun `case1 - x starts before y and overlaps`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t2, t4)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case2 - y starts before x and overlaps`() {
            val x = LocalDateInterval(t2, t4)
            val y = LocalDateInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case3 - x and y touch at start of x`() {
            val x = LocalDateInterval(t3, t4)
            val y = LocalDateInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case4 - x and y touch at end of x`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case5 - x starts after y and both have the same end`() {
            val x = LocalDateInterval(t2, t3)
            val y = LocalDateInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case6 - x ends before y and both have the same start`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t1, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case7 - x ends after y and both have the same start`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t1, t2)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case8 - x starts before y and both have the same end`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case9 - x contains y`() {
            val x = LocalDateInterval(t1, t4)
            val y = LocalDateInterval(t2, t3)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case10 - y contains x`() {
            val x = LocalDateInterval(t2, t3)
            val y = LocalDateInterval(t1, t4)

            assertTrue { x overlaps y }
        }

        @Test
        fun `case11 - x is after y`() {
            val x = LocalDateInterval(t3, t4)
            val y = LocalDateInterval(t1, t2)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case12 - y is after x`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t3, t4)

            assertFalse { x overlaps y }
        }

        @Test
        fun `case13 - x equals y`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t1, t2)

            assertTrue { x overlaps y }
        }
    }


    class OverlapTests {
        val t1 = LocalDate.parse("2000-01-01")
        val t2 = LocalDate.parse("2000-02-01")
        val t3 = LocalDate.parse("2000-03-01")
        val t4 = LocalDate.parse("2000-04-01")

        @Test
        fun `case1 - x starts before y and overlaps`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t2, t4)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case2 - y starts before x and overlaps`() {
            val x = LocalDateInterval(t2, t4)
            val y = LocalDateInterval(t1, t3)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case3 - x and y touch at start of x`() {
            val x = LocalDateInterval(t3, t4)
            val y = LocalDateInterval(t2, t3)

            assertEquals( LocalDateInterval(t3, t3), x overlap y )
        }

        @Test
        fun `case4 - x and y touch at end of x`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t2, t3)

            assertEquals( LocalDateInterval(t2, t2), x overlap y )
        }

        @Test
        fun `case5 - x starts after y and both have the same end`() {
            val x = LocalDateInterval(t2, t3)
            val y = LocalDateInterval(t1, t3)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case6 - x ends before y and both have the same start`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t1, t3)

            assertEquals( LocalDateInterval(t1, t2), x overlap y )
        }

        @Test
        fun `case7 - x ends after y and both have the same start`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t1, t2)

            assertEquals( LocalDateInterval(t1, t2), x overlap y )
        }

        @Test
        fun `case8 - x starts before y and both have the same end`() {
            val x = LocalDateInterval(t1, t3)
            val y = LocalDateInterval(t2, t3)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case9 - x contains y`() {
            val x = LocalDateInterval(t1, t4)
            val y = LocalDateInterval(t2, t3)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case10 - y contains x`() {
            val x = LocalDateInterval(t2, t3)
            val y = LocalDateInterval(t1, t4)

            assertEquals( LocalDateInterval(t2, t3), x overlap y )
        }

        @Test
        fun `case11 - x is after y`() {
            val x = LocalDateInterval(t3, t4)
            val y = LocalDateInterval(t1, t2)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case12 - y is after x`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t3, t4)

            assertEquals( null, x overlap y )
        }

        @Test
        fun `case13 - x equals y`() {
            val x = LocalDateInterval(t1, t2)
            val y = LocalDateInterval(t1, t2)

            assertEquals( LocalDateInterval(t1, t2), x overlap y )
        }
    }
}