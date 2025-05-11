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
import java.time.LocalDateTime

class LocalDateTimeIntervalMap<T: Any> {

    private val delegate = TreeRangeMap.create<LocalDateTime, T>()

    operator fun get(key: LocalDateTime) = delegate[key]

    fun isEmpty(): Boolean {
        val empty = delegate.asMapOfRanges().isEmpty()
        if (empty) return true

        return delegate.asMapOfRanges().keys.all { it.isEmpty }
    }

    fun asMapOfLocalDateTimeIntervals() : Map<LocalDateTimeInterval, T> {
        return delegate.asMapOfRanges()
                .mapKeys { toLocalDateTimeInterval(it.key) }
    }

    private fun toLocalDateTimeInterval(range: Range<LocalDateTime>) =
            LocalDateTimeInterval(range.lowerEndpoint(), range.upperEndpoint())

    fun clear() = delegate.clear()

    fun put(key: LocalDateTimeInterval, value: T) {
        delegate.putCoalescing(key.toRange(), value)
    }

    fun putAll(from: LocalDateTimeIntervalMap<T>) {
        from.delegate.asMapOfRanges().entries.forEach { delegate.put(it.key, it.value)}
    }

    fun remove(key: LocalDateTimeInterval) {
        delegate.remove(key.toRange())
    }

    fun size() = delegate.asMapOfRanges().size

    fun span() = toLocalDateTimeInterval(delegate.span())
}
