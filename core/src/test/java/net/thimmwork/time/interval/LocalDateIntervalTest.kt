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
import java.time.LocalDate.parse
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateIntervalTest {
    private val interval2018 = localDateInterval("2018-01-01", "2018-12-31")

    @Test
    fun `Infiniy contains the year 2018`() {
        val infinity = Infinity.LOCAL_DATE_INTERVAL

        assertTrue { infinity.contains(interval2018) }
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = LocalDateInterval(parse("1900-01-01"), parse("1999-12-31"))
        val normalized = twentiethCentury.normalize()

        val expected = LocalDateInterval(parse("1970-01-01"), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 10k is normalized to dec 31th 4000`() {
        val hugeInterval = LocalDateInterval(parse("2000-01-01"), parse("5000-01-01"))
        val normalized = hugeInterval.normalize()

        val expected = LocalDateInterval(hugeInterval.start, parse("4000-12-31"))
        assertEquals(expected, normalized)
    }

    @Test
    fun `2018 contains first and last day of the year, but not first day of 2019`() {
        assertTrue { interval2018.contains(parse("2018-01-01")) }
        assertTrue { interval2018.contains(parse("2018-12-31")) }
        assertFalse { interval2018.contains(parse("2019-01-01")) }
    }

    @Test
    fun `LocalDateInterval with start=end contains exactly that date`() {
        val date = parse("2018-01-01")
        val interval = LocalDateInterval(date, date)
        assertTrue { interval.contains(date) }
        assertFalse { interval.contains(date.minusDays(1)) }
        assertFalse { interval.contains(date.plusDays(1)) }
    }

    @Test(expected = IllegalArgumentException::class)
    fun `attempt to create interval with end before start will throw IllegalArgumentException`() {
        localDateInterval("2018-12-31", "2018-01-01")
    }

    @Test
    fun `conversion to range creates a closed range`() {
        val range2018 = interval2018.toRange()
        assertTrue { range2018.hasLowerBound() }
        assertTrue { range2018.lowerBoundType() == BoundType.CLOSED }
        assertTrue { range2018.lowerEndpoint() == parse("2018-01-01") }
        assertTrue { range2018.hasUpperBound() }
        assertTrue { range2018.upperBoundType() == BoundType.CLOSED }
        assertTrue { range2018.upperEndpoint() == parse("2018-12-31") }
    }
}