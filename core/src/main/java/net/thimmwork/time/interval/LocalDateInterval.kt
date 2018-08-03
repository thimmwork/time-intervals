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
import java.util.*

class LocalDateInterval(
        start: LocalDate = Infinity.MIN_DATE,
        end: LocalDate = Infinity.MAX_DATE
) : AbstractInterval<LocalDate>(Range.closed(start, end)), Iterable<LocalDate>, ClosedRange<LocalDate> {
    override val endInclusive get() = interval.upperEndpoint()

    override fun contains(value: LocalDate) = super<AbstractInterval>.contains(value)

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

    override fun iterator() : Iterator<LocalDate> = ForwardLocalDateIterator()

    infix fun step(step: Long) : Iterable<LocalDate> {
        if (step > 0) {
            return Iterable { ForwardLocalDateIterator(step) }
        } else {
            return Iterable { ReverseLocalDateIterator(step) }
        }
    }

    private inner class ReverseLocalDateIterator(
            private val step: Long
    ) : Iterator<LocalDate> {
        private var next = end

        override fun hasNext() = !next.isBefore(start)

        override fun next(): LocalDate {
            if (hasNext()) {
                return next.also {
                    next = next.plusDays(step)
                }
            }
            throw NoSuchElementException()
        }
    }

    private inner class ForwardLocalDateIterator(
            private val step: Long = 1
    ) : Iterator<LocalDate> {
        protected var next = start

        override fun hasNext() = !next.isAfter(end)

        override fun next(): LocalDate {
            if (hasNext()) {
                return next.also {
                    next = next.plusDays(step)
                }
            }
            throw NoSuchElementException()
        }
    }
}

operator fun LocalDate.rangeTo(other: LocalDate) = LocalDateInterval(this, other)

fun localDateInterval(dateStart: String, dateEnd: String): LocalDateInterval {
    return LocalDateInterval(LocalDate.parse(dateStart), LocalDate.parse(dateEnd))
}

infix fun LocalDate.downTo(start: LocalDate): Iterable<LocalDate> {
    return LocalDateInterval(start, this).step(-1)
}

