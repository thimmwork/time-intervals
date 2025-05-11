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
import com.google.common.collect.TreeRangeMap
import java.time.LocalDate

class LocalDateIntervalMap<T: Any> {

    private val delegate = TreeRangeMap.create<LocalDate, T>()

    operator fun get(key: LocalDate) = delegate[key]

    operator fun get(key: LocalDateInterval) : Collection<T> = delegate.subRangeMap(key.toRange()).asMapOfRanges().values

    fun isEmpty(): Boolean {
        val empty = delegate.asMapOfRanges().isEmpty()
        if (empty) return true

        return delegate.asMapOfRanges().keys.all { it.isEmpty }
    }

    fun asMapOfLocalDateIntervals() : Map<LocalDateInterval, T> {
        return delegate.asMapOfRanges()
                .mapKeys { toLocalDateInterval(it.key) }
    }

    fun clear() = delegate.clear()

    fun put(key: LocalDateInterval, value: T) {
        delegate.put(key.toRange(), value)
    }

    fun putAll(from: LocalDateIntervalMap<T>) {
        from.delegate.asMapOfRanges().entries.forEach { delegate.put(it.key, it.value)}
    }

    fun remove(key: LocalDateInterval) {
        val toRange = key.toRange()
        val range = Range.open(toRange.lowerEndpoint().minusDays(1), toRange.upperEndpoint().plusDays(1))
        delegate.remove(range)
    }
}
