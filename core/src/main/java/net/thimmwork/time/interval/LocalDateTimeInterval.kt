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
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class LocalDateTimeInterval(
        start: LocalDateTime = Infinity.MIN_DATE_TIME,
        end: LocalDateTime = Infinity.MAX_DATE_TIME
) : AbstractInterval<LocalDateTime>(Range.closedOpen(start, end)) {

    fun normalize(): LocalDateTimeInterval {
        val minDate = Infinity.LOCAL_DATE_TIME_INTERVAL.start
        val maxDate = Infinity.LOCAL_DATE_TIME_INTERVAL.end
        val start = if (start.isBefore(minDate))
            minDate
        else
            start
        val end = if (end.isAfter(maxDate))
            maxDate
        else
            end
        return LocalDateTimeInterval(start, end)
    }

    fun contains(other: LocalDateTimeInterval) : Boolean {
        return !this.start.isAfter(other.start) && !this.end.isBefore(other.end)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LocalDateTimeInterval

        if (interval != other.interval) return false

        return true
    }

    override fun hashCode() = interval.hashCode()

    override fun toString() = "LocalDateTimeInterval(interval=$interval)"
}

operator fun LocalDateTime.rangeTo(other: LocalDateTime) = LocalDateTimeInterval(this, other)

fun localDateTimeInterval(dateStart: String, dateEnd: String): LocalDateTimeInterval {
    return LocalDateTimeInterval(LocalDateTime.parse(dateStart), LocalDateTime.parse(dateEnd))
}
