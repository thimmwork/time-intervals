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
import java.time.temporal.Temporal

open class AbstractInterval<T>(protected val interval: Range<T>) where T : Temporal, T: Comparable<T> {
    val start get() = interval.lowerEndpoint()
    val end get() = interval.upperEndpoint()

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AbstractInterval<*>

        if (interval != other.interval) return false

        return true
    }

    fun contains(other: AbstractInterval<T>) : Boolean {
        return this.start.compareTo(other.start) <= 0 && this.end.compareTo(other.end) >= 0
    }

    fun contains(other: T) = interval.contains(other)

    override fun hashCode() = interval.hashCode()
    override fun toString() = "Interval(start=$start,end=$end)"
}
