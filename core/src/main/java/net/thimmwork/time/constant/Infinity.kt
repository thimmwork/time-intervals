package net.thimmwork.time.constant

import net.thimmwork.time.interval.InstantInterval
import net.thimmwork.time.interval.LocalDateInterval
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

object Infinity {
    @JvmStatic val MIN_DATE = LocalDate.of(1970, 1, 1);
    @JvmStatic val MAX_DATE = LocalDate.of(4000, 12, 31);
    @JvmStatic val LOCAL_DATE_INTERVAL = LocalDateInterval(MIN_DATE, MAX_DATE)
    @JvmStatic val INSTANT_INTERVAL = InstantInterval(Instant.EPOCH, MAX_DATE.atStartOfDay().toInstant(ZoneOffset.UTC))
}
