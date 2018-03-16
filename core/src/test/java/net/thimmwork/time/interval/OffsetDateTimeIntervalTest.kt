package net.thimmwork.time.interval

import net.thimmwork.time.constant.Infinity
import org.junit.Test
import java.time.OffsetDateTime.parse
import java.time.ZoneId
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class OffsetDateTimeIntervalTest {
    @Test
    fun `Infiniy contains the year 2018 CET`() {
        val infinity = Infinity.INSTANT_INTERVAL

        val year2018withCEToffset = OffsetDateTimeInterval(parse("2018-01-01T00:00:00+01:00"), parse("2018-12-31T00:00:00+01:00"))
        assertTrue { infinity.contains(year2018withCEToffset.toInstantInterval()) }
    }

    @Test
    fun `the 20th century is normalized to 1970-2000`() {
        val twentiethCentury = OffsetDateTimeInterval(parse("1900-01-01T00:00:00+01:00"), parse("1999-12-31T00:00:00+01:00"))
        val normalized = twentiethCentury.normalize()

        val expected = OffsetDateTimeInterval(parse("1970-01-01T01:00:00+01:00"), twentiethCentury.end)
        assertEquals(expected, normalized)
    }

    @Test
    fun `an interval up to year 10k is normalized to dec 31th 4000`() {
        val hugeInterval = OffsetDateTimeInterval(parse("2000-01-01T00:00:00+01:00"), parse("5000-01-01T00:00:00+01:00"))
        val normalized = hugeInterval.normalize()

        val expected = OffsetDateTimeInterval(hugeInterval.start, parse("4000-12-31T01:00:00+01:00"))
        assertEquals(expected, normalized)
    }

}