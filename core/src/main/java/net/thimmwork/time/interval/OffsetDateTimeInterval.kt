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

    fun toLocalDateTimeInterval() : LocalDateTimeInterval {
        return LocalDateTimeInterval(start.toLocalDateTime(), end.toLocalDateTime())
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

    companion object {
        fun parse(start: String, end: String) : OffsetDateTimeInterval {
            return OffsetDateTimeInterval(OffsetDateTime.parse(start), OffsetDateTime.parse(end))
        }
    }
}
