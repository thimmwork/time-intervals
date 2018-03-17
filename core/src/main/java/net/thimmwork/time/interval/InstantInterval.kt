package net.thimmwork.time.interval

import com.google.common.collect.Range
import net.thimmwork.time.constant.Infinity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class InstantInterval(
        start: Instant = Infinity.INSTANT_INTERVAL.start,
        end: Instant = Infinity.INSTANT_INTERVAL.end
) {
    private val interval: Range<Instant>;

    init {
        interval = Range.closedOpen(start, end)
    }

    val start get() = interval.lowerEndpoint()
    val end get() = interval.upperEndpoint()

    fun normalize(): InstantInterval {
        val minInstant = Infinity.INSTANT_INTERVAL.start
        val maxInstant = Infinity.INSTANT_INTERVAL.end
        val start = if (start.isBefore(minInstant))
            minInstant
        else
            start
        val end = if (end.isAfter(maxInstant))
            maxInstant
        else
            end
        return InstantInterval(start, end)
    }

    fun contains(other: InstantInterval) : Boolean {
        return !this.start.isAfter(other.start) && !this.end.isBefore(other.end)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstantInterval

        if (interval != other.interval) return false

        return true
    }

    override fun hashCode() = interval.hashCode()

    override fun toString() = "InstantInterval(begin=$start,end=$end)"
}

fun instantInterval(dateTimeStart: String, dateTimeEnd: String, zoneId: ZoneId): InstantInterval {
    return InstantInterval(instant(dateTimeStart, zoneId), instant(dateTimeEnd, zoneId))
}

fun instant(dateTime: String, zoneId: ZoneId): Instant {
    return LocalDateTime.parse(dateTime).atZone(zoneId).toInstant()
}
