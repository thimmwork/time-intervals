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

import net.thimmwork.time.interval.LocalDateTimeInterval.Companion.parse
import org.junit.Assert.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LocalDateTimeIntervalMapTest {
    private val interval2018 = parse("2018-01-01T00:00:00", "2019-01-01T00:00:00")
    private val firstHalf2018 = parse("2018-01-01T00:00:00", "2018-07-01T00:00:00")
    private val secondHalf2018 = parse("2018-07-01T00:00:00", "2019-01-01T00:00:00")

    @Test
    fun `get returns a previously put value for both bounds and null for date beyond bound`() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(interval2018, 4711)

        assertTrue { map[interval2018.start] == 4711 }
        assertTrue { map[interval2018.end.minusSeconds(1)] == 4711 }

        assertTrue { map[interval2018.start.minusSeconds(1)] == null }
        assertTrue { map[interval2018.end] == null }
    }

    @Test
    fun `when a map is created then it is empty`() {
        val map = LocalDateTimeIntervalMap<Int>()
        assertTrue { map.isEmpty() }
    }

    @Test
    fun `when an interval is put then the map is not empty - when the interval is removed then it is empty again`() {
        val map = LocalDateTimeIntervalMap<Int>()

        map.put(interval2018, 4711)
        assertFalse { map.isEmpty() }

        map.remove(interval2018)
        assertTrue { map.isEmpty() }
    }

    @Test
    fun `when all partial intervals are removed then the map is empty`() {
        val map = LocalDateTimeIntervalMap<Int>()

        map.put(interval2018, 4711)

        map.remove(firstHalf2018)
        assertFalse { map.isEmpty() }
        map.remove(secondHalf2018)
        assertTrue { map.isEmpty() }
    }

    @Test
    fun asMapOfLocalDateTimeIntervals() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        val asMapOfLocalDateTimeIntervals = map.asMapOfLocalDateTimeIntervals()

        assertTrue { !asMapOfLocalDateTimeIntervals.isEmpty() }
        assertTrue { asMapOfLocalDateTimeIntervals.size == 2  }
        assertTrue { asMapOfLocalDateTimeIntervals == mapOf(Pair(firstHalf2018, 4711), Pair(secondHalf2018, 4712))  }
    }

    @Test
    fun clear() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        map.clear()

        assertTrue { map.isEmpty() }
        assertTrue { map.asMapOfLocalDateTimeIntervals().isEmpty() }
    }

    @Test
    fun putAll() {
        val map = LocalDateTimeIntervalMap<Int>()
        val otherMap = LocalDateTimeIntervalMap<Int>()
        otherMap.put(firstHalf2018, 4711)
        otherMap.put(secondHalf2018, 4712)

        map.putAll(otherMap)
        otherMap.remove(secondHalf2018)

        assertFalse { otherMap[secondHalf2018.start] == 4712 }
        assertTrue { map[secondHalf2018.start] == 4712 }
    }

    @Test
    fun `put replaces existing values`() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(interval2018, 1)

        map.put(firstHalf2018, 2)

        val mapOfLocalDateIntervals = map.asMapOfLocalDateTimeIntervals()
        assertTrue { mapOfLocalDateIntervals[firstHalf2018] == 2}
        assertTrue { mapOfLocalDateIntervals[secondHalf2018] == 1}
    }

    @Test
    fun size() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(firstHalf2018, 1)
        map.put(secondHalf2018, 1)

        assertTrue { map.size() == 1 }
    }

    @Test
    fun span() {
        val map = LocalDateTimeIntervalMap<Int>()
        map.put(firstHalf2018, 1)

        assertTrue { map.span() == firstHalf2018 }

        val newStart = LocalDateTime.parse("2000-01-17T23:49:03")
        map.put(LocalDateTimeInterval(newStart, LocalDateTime.parse("2009-03-01T00:00:00")), 2)

        assertTrue { map.span() == LocalDateTimeInterval(newStart, firstHalf2018.end) }
    }

    @Test
    fun `span throws NoSuchElementException when empty`() {
        assertThrows(NoSuchElementException::class.java) {
            LocalDateTimeIntervalMap<Int>().span()
        }
    }
}
