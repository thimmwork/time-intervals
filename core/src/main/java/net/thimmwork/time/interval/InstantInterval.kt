/**
 * Copyright 2018 thimmwork
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.thimmwork.time.interval

import com.google.common.collect.Range
import net.thimmwork.time.constant.Infinity
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

class InstantInterval private constructor(range: Range<Instant>) : AbstractInterval<Instant>(range) {

    constructor(start: Instant = Infinity.INSTANT_INTERVAL.start, end: Instant = Infinity.INSTANT_INTERVAL.end)
            : this(Range.closedOpen(start, end))

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

    override fun overlap(other: AbstractInterval<Instant>): InstantInterval? {
        val overlappingRange = overlappingRange(other)
        return if (overlappingRange == null) null else InstantInterval(overlappingRange)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstantInterval

        if (interval != other.interval) return false

        return true
    }

    override fun hashCode() = interval.hashCode()
}

fun instantInterval(dateTimeStart: String, dateTimeEnd: String, zoneId: ZoneId): InstantInterval {
    return InstantInterval(instant(dateTimeStart, zoneId), instant(dateTimeEnd, zoneId))
}

fun instant(dateTime: String, zoneId: ZoneId): Instant {
    return LocalDateTime.parse(dateTime).atZone(zoneId).toInstant()
}
