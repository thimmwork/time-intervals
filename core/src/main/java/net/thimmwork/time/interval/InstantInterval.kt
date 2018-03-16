package net.thimmwork.time.interval

import net.thimmwork.time.constant.Infinity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

data class InstantInterval(
        var start: Instant = Infinity.INSTANT_INTERVAL.start,
        var end: Instant = Infinity.INSTANT_INTERVAL.end
) {
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
}

fun instantInterval(dateTimeStart: String, dateTimeEnd: String, zoneId: ZoneId): InstantInterval {
    return InstantInterval(instant(dateTimeStart, zoneId), instant(dateTimeEnd, zoneId))
}

fun instant(dateTime: String, zoneId: ZoneId): Instant {
    return LocalDateTime.parse(dateTime).atZone(zoneId).toInstant()
}
