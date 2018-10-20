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
import java.time.OffsetDateTime

class OffsetDateTimeIntervalMap<T> {

    private val delegate = TreeRangeMap.create<OffsetDateTime, T>()

    operator fun get(key: OffsetDateTime) = delegate[key]

    fun isEmpty(): Boolean {
        val empty = delegate.asMapOfRanges().isEmpty()
        if (empty) return true

        return delegate.asMapOfRanges().keys.all { it.isEmpty }
    }

    fun asMapOfOffsetDateTimeIntervals() : Map<OffsetDateTimeInterval, T> {
        return delegate.asMapOfRanges()
                .mapKeys { toOffsetDateTimeInterval(it.key) }
    }

    private fun toOffsetDateTimeInterval(range: Range<OffsetDateTime>) =
            OffsetDateTimeInterval(range.lowerEndpoint(), range.upperEndpoint())

    fun clear() = delegate.clear()

    fun put(key: OffsetDateTimeInterval, value: T) {
        delegate.put(key.toRange(), value)
    }

    fun putAll(from: OffsetDateTimeIntervalMap<T>) {
        from.delegate.asMapOfRanges().entries.forEach { delegate.put(it.key, it.value)}
    }

    fun remove(key: OffsetDateTimeInterval) {
        delegate.remove(key.toRange())
    }
}
