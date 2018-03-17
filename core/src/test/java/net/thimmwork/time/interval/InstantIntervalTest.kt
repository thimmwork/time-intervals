package net.thimmwork.time.interval

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
}