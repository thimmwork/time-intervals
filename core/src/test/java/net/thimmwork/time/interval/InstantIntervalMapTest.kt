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

import net.thimmwork.time.interval.InstantInterval.Companion.parse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.Instant
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class InstantIntervalMapTest {
    private val interval2018 = parse("2018-01-01T00:00:00Z", "2019-01-01T00:00:00Z")
    private val firstHalf2018 = parse("2018-01-01T00:00:00Z", "2018-07-01T00:00:00Z")
    private val secondHalf2018 = parse("2018-07-01T00:00:00Z", "2019-01-01T00:00:00Z")

    @Test
    fun `get returns a previously put value for both bounds and null for date beyond bound`() {
        val map = InstantIntervalMap<Int>()
        map.put(interval2018, 4711)

        assertTrue { map[interval2018.start] == 4711 }
        assertTrue { map[interval2018.end.minusSeconds(1)] == 4711 }

        assertTrue { map[interval2018.start.minusSeconds(1)] == null }
        assertTrue { map[interval2018.end] == null }
    }

    @Test
    fun `when a map is created then it is empty`() {
        val map = InstantIntervalMap<Int>()
        assertTrue { map.isEmpty() }
    }

    @Test
    fun `when an interval is put then the map is not empty - when the interval is removed then it is empty again`() {
        val map = InstantIntervalMap<Int>()

        map.put(interval2018, 4711)
        assertFalse { map.isEmpty() }

        map.remove(interval2018)
        assertTrue { map.isEmpty() }
    }

    @Test
    fun `when all partial intervals are removed then the map is empty`() {
        val map = InstantIntervalMap<Int>()

        map.put(interval2018, 4711)

        map.remove(firstHalf2018)
        assertFalse { map.isEmpty() }
        map.remove(secondHalf2018)
        assertTrue { map.isEmpty() }
    }

    @Test
    fun asMapOfInstantIntervals() {
        val map = InstantIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        val asMapOfInstantIntervals = map.asMapOfInstantIntervals()

        assertTrue { !asMapOfInstantIntervals.isEmpty() }
        assertTrue { asMapOfInstantIntervals.size == 2  }
        assertTrue { asMapOfInstantIntervals == mapOf(Pair(firstHalf2018, 4711), Pair(secondHalf2018, 4712))  }
    }

    @Test
    fun clear() {
        val map = InstantIntervalMap<Int>()
        map.put(firstHalf2018, 4711)
        map.put(secondHalf2018, 4712)

        map.clear()

        assertTrue { map.isEmpty() }
        assertTrue { map.asMapOfInstantIntervals().isEmpty() }
    }

    @Test
    fun putAll() {
        val map = InstantIntervalMap<Int>()
        val otherMap = InstantIntervalMap<Int>()
        otherMap.put(firstHalf2018, 4711)
        otherMap.put(secondHalf2018, 4712)

        map.putAll(otherMap)
        otherMap.remove(secondHalf2018)

        assertFalse { otherMap[secondHalf2018.start] == 4712 }
        assertTrue { map[secondHalf2018.start] == 4712 }
    }

    @Test
    fun `put replaces existing values`() {
        val map = InstantIntervalMap<Int>()
        map.put(interval2018, 1)

        map.put(firstHalf2018, 2)

        val mapOfLocalDateIntervals = map.asMapOfInstantIntervals()
        assertTrue { mapOfLocalDateIntervals[firstHalf2018] == 2}
        assertTrue { mapOfLocalDateIntervals[secondHalf2018] == 1}
    }

    @Test
    fun size() {
        val map = InstantIntervalMap<Int>()
        map.put(firstHalf2018, 1)
        map.put(secondHalf2018, 1)

        assertTrue { map.size() == 1 }
    }

    @Test
    fun span() {
        val map = InstantIntervalMap<Int>()
        map.put(firstHalf2018, 1)

        assertTrue { map.span() == firstHalf2018 }

        val newStart = Instant.parse("2000-01-17T23:49:03Z")
        map.put(InstantInterval(newStart, Instant.parse("2009-03-01T00:00:00Z")), 2)

        assertTrue { map.span() == InstantInterval(newStart, firstHalf2018.end) }
    }

    @Test
    fun `span throws NoSuchElementException when empty`() {
        assertThrows<NoSuchElementException> {
            InstantIntervalMap<Int>().span()
        }
    }
}
