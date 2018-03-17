package net.thimmwork.time.interval

import com.google.common.collect.Range
import net.thimmwork.time.constant.Infinity
import java.time.OffsetDateTime

class OffsetDateTimeInterval(
        start: OffsetDateTime,
        end: OffsetDateTime
) : AbstractInterval<OffsetDateTime>(Range.closedOpen(start, end)) {

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OffsetDateTimeInterval

        if (interval != other.interval) return false

        return true
    }

    override fun hashCode() = interval.hashCode()

    override fun toString() = "OffsetDateTimeInterval(interval=$interval)"
}
