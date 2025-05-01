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

import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDate.parse
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SimpleLocalDateIteratorTest {
    @Test
    fun `simple iteration from sunday to saturday contains all days of the week`() {
        val interval = LocalDateInterval(parse("2018-03-11"), parse("2018-03-17"))

        val sut = interval.iterator()

        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-11"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-12"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-13"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-14"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-15"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-16"), sut.next())
        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-17"), sut.next())
        assertFalse { sut.hasNext() }

        val resultListIterable = ArrayList<LocalDate>(7)
        for (date in interval) {
            resultListIterable.add(date)
        }
        assertEquals(resultListIterable, listOf(
                parse("2018-03-11"),
                parse("2018-03-12"),
                parse("2018-03-13"),
                parse("2018-03-14"),
                parse("2018-03-15"),
                parse("2018-03-16"),
                parse("2018-03-17")))
    }

    @Test
    fun `kotlins range progression can be used to iterate over an interval`() {

        val resultListIterable = ArrayList<LocalDate>(7)

        val start = parse("2018-03-11")
        val end = parse("2018-03-17")
        for (date in start..end) {
            resultListIterable.add(date)
        }
        assertEquals(resultListIterable, listOf(
                parse("2018-03-11"),
                parse("2018-03-12"),
                parse("2018-03-13"),
                parse("2018-03-14"),
                parse("2018-03-15"),
                parse("2018-03-16"),
                parse("2018-03-17")))
    }

    @Test
    fun `kotlins step can be used on interval progressions`() {

        val resultListIterable = ArrayList<LocalDate>(7)

        val start = parse("2018-03-11")
        val end = parse("2018-03-18")
        for (date in start..end step 3) {
            resultListIterable.add(date)
        }
        assertEquals(resultListIterable, listOf(
                parse("2018-03-11"),
                parse("2018-03-14"),
                parse("2018-03-17")))
    }

    @Test
    fun `negative steps can be used to iterate from end to start`() {

        val resultListReverse = ArrayList<LocalDate>(7)

        val start = parse("2018-03-11")
        val end = parse("2018-03-17")
        for (date in start..end step -1) {
            resultListReverse.add(date)
        }
        assertEquals(resultListReverse, listOf(
                parse("2018-03-17"),
                parse("2018-03-16"),
                parse("2018-03-15"),
                parse("2018-03-14"),
                parse("2018-03-13"),
                parse("2018-03-12"),
                parse("2018-03-11")))
    }

    @Test
    fun `downTo can be used to iterate from end to start`() {

        val resultListReverse = ArrayList<LocalDate>(7)

        val start = parse("2018-03-11")
        val end = parse("2018-03-17")
        for (date in end downTo start) {
            resultListReverse.add(date)
        }
        assertEquals(resultListReverse, listOf(
                parse("2018-03-17"),
                parse("2018-03-16"),
                parse("2018-03-15"),
                parse("2018-03-14"),
                parse("2018-03-13"),
                parse("2018-03-12"),
                parse("2018-03-11")))
    }

    @Test
    fun `an interval with same start and end day iterates only over exactly that day`() {
        val interval = LocalDateInterval(parse("2018-03-18"), parse("2018-03-18"))

        val sut = interval.iterator()

        assertTrue { sut.hasNext() }
        assertEquals(parse("2018-03-18"), sut.next())
        assertFalse { sut.hasNext() }
    }
}
