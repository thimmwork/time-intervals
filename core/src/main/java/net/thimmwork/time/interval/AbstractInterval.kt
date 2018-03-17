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

    override fun hashCode() = interval.hashCode()
    override fun toString() = "Interval(start=$start,end=$end)"
}
