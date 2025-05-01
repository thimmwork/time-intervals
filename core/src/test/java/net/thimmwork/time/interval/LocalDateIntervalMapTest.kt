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

import net.thimmwork.time.interval.LocalDateInterval.Companion.parse
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.IsCollectionContaining.hasItems
import org.junit.jupiter.api.Test
import java.time.LocalDate
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateIntervalMapTest {
    private val interval2018 = parse("2018-01-01", "2018-12-31")
    private val firstHalf2018 = parse("2018-01-01", "2018-06-30")
    private val secondHalf2018 = parse("2018-07-01", "2018-12-31")

    @Test
    fun `get returns a previously put value for both bounds and null for date beyond bound`() {
        val map = LocalDateIntervalMap<Int>()
        map.put(interval2018, 4711)

        assertTrue { map[interval2018.start] == 4711 }
        assertTrue { map[interval2018.end] == 4711 }

        assertTrue { map[interval2018.start-1] == null }
        assertTrue { map[interval2018.end+1] == null }
    }

    @org.junit.jupiter.api.Test
    fun `when a map is created then it is empty`() {
        val map = LocalDateIntervalMap<Int>()
        assertTrue { map.isEmpty() }
    }

    @org.junit.jupiter.api.Test
    fun `when an interval is put then the map is not empty - when the interval is removed then it is empty again`() {
        val map = LocalDateIntervalMap<Int>()

        map.put(interval2018, 4711)
        assertFalse { map.isEmpty() }

        map.remove(interval2018)
        assertTrue { map.isEmpty() }
    }

    @org.junit.jupiter.api.Test
    fun `when all partial intervals are removed then the map is empty`() {
        val map = LocalDateIntervalMap<Int>()

        map.put(interval2018, 4711)

        map.remove(firstHalf2018)
        assertFalse { map.isEmpty() }
        map.remove(secondHalf2018)
        assertTrue { map.isEmpty() }
    }

    @Test
    fun asMapOfLocalDateIntervals() {
        val map = LocalDateIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        val asMapOfLocalDateIntervals = map.asMapOfLocalDateIntervals()

        assertTrue { !asMapOfLocalDateIntervals.isEmpty() }
        assertTrue { asMapOfLocalDateIntervals.size == 2  }
        assertTrue { asMapOfLocalDateIntervals == mapOf(Pair(firstHalf2018, 4711), Pair(secondHalf2018, 4712))  }
    }

    @org.junit.jupiter.api.Test
    fun clear() {
        val map = LocalDateIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        map.clear()

        assertTrue { map.isEmpty() }
        assertTrue { map.asMapOfLocalDateIntervals().isEmpty() }
    }

    @Test
    fun putAll() {
        val map = LocalDateIntervalMap<Int>()
        val otherMap = LocalDateIntervalMap<Int>()
        otherMap.put(firstHalf2018, 4711)
        otherMap.put(secondHalf2018, 4712)

        map.putAll(otherMap)
        otherMap.remove(secondHalf2018)

        assertFalse { otherMap.get(secondHalf2018.start) == 4712 }
        assertTrue { map.get(secondHalf2018.start) == 4712 }
    }

    @org.junit.jupiter.api.Test
    fun `put replaces existing values`() {
        val map = LocalDateIntervalMap<Int>()
        map.put(interval2018, 1)

        map.put(firstHalf2018, 2)

        val mapOfLocalDateIntervals = map.asMapOfLocalDateIntervals()
        assertTrue { mapOfLocalDateIntervals[firstHalf2018] == 2}
        assertTrue { map[firstHalf2018.start] == 2 }
        assertTrue { map[firstHalf2018.end] == 2 }
        assertTrue { mapOfLocalDateIntervals[secondHalf2018] == 1}
        assertTrue { map[secondHalf2018.start] == 1}
        assertTrue { map[secondHalf2018.end] == 1}
    }

    @org.junit.jupiter.api.Test
    fun `get with interval returns a list of values associated with the interval`() {
        val map = LocalDateIntervalMap<Int>()
        map.put(parse("2018-12-01", "2018-12-29"), -1)
        map.put(parse("2018-12-30", "2019-01-01"), 1)
        map.put(parse("2019-01-03", "2019-01-03"), 2)
        map.put(parse("2019-01-04", "2019-01-04"), 3)
        map.put(parse("2019-02-01", "2019-02-01"), -1)

        val result = map[parse("2019-01-01", "2019-01-31")]

        assertThat(result, hasItems(1, 2, 3))
    }
}

operator fun LocalDate.minus(daysToSubtract: Long) = this.minusDays(daysToSubtract)
operator fun LocalDate.plus(daysToAdd: Long) = this.plusDays(daysToAdd)
