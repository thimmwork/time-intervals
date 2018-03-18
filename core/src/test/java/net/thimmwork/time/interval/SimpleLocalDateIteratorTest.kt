package net.thimmwork.time.interval

import org.junit.Test
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