package net.thimmwork.time.interval

import net.thimmwork.time.constant.Infinity
import org.junit.Test
import java.time.LocalDate.parse
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalDateIntervalTest {
    @Test
    fun `Infiniy contains the year 2018`() {
        val infinity = Infinity.LOCAL_DATE_INTERVAL

        assertTrue { infinity.contains(localDateInterval("2018-01-01", "2018-12-31")) }
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

}