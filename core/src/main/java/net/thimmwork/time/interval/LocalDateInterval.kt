package net.thimmwork.time.interval

import net.thimmwork.time.constant.Infinity
import java.time.LocalDate

data class LocalDateInterval(
        val start: LocalDate = Infinity.MIN_DATE,
        val end: LocalDate = Infinity.MAX_DATE
) {
    fun normalize(): LocalDateInterval {
        val minDate = Infinity.LOCAL_DATE_INTERVAL.start
        val maxDate = Infinity.LOCAL_DATE_INTERVAL.end
        val start = if (start.isBefore(minDate))
            minDate
        else
            start
        val end = if (end.isAfter(maxDate))
            maxDate
        else
            end
        return LocalDateInterval(start, end)
    }

    fun contains(other: LocalDateInterval) : Boolean {
        return !this.start.isAfter(other.start) && !this.end.isBefore(other.end)
    }
}

fun localDateInterval(dateStart: String, dateEnd: String): LocalDateInterval {
    return LocalDateInterval(LocalDate.parse(dateStart), LocalDate.parse(dateEnd))
}
