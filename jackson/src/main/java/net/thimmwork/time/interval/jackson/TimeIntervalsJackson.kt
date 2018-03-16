package net.thimmwork.time.interval.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import net.thimmwork.time.interval.InstantInterval
import net.thimmwork.time.interval.LocalDateInterval

val MODULE = SimpleModule(
        "time-intervals-jackson",
        Version(1, 0, 0, "RELEASE", "net.thimmwork.time", "intervals-jackson"),
        ImmutableMap.of<Class<*>, JsonDeserializer<*>>(
                InstantInterval::class.java, InstantIntervalDeserializer(),
                LocalDateInterval::class.java, LocalDateIntervalDeserializer()
        ),
        ImmutableList.of<JsonSerializer<*>>(
                InstantIntervalSerializer(),
                LocalDateIntervalSerializer()
        ))
