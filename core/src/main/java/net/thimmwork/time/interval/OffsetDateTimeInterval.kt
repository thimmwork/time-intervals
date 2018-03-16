package net.thimmwork.time.interval

import net.thimmwork.time.constant.Infinity
import java.time.OffsetDateTime

data class OffsetDateTimeInterval(val start: OffsetDateTime, val end: OffsetDateTime) {

    fun normalize(): OffsetDateTimeInterval {
        val minInstant = Infinity.INSTANT_INTERVAL.start
        val maxInstant = Infinity.INSTANT_INTERVAL.end
        val start = if (start.toInstant().isBefore(minInstant))
            minInstant
        else
            start.toInstant()
        val end = if (end.toInstant().isAfter(maxInstant))
            maxInstant
        else
            end.toInstant()
        return OffsetDateTimeInterval(start.atOffset(this.start.getOffset()), end.atOffset(this.end.getOffset()))
    }

    fun toInstantInterval() = InstantInterval(start.toInstant(), end.toInstant())
}
