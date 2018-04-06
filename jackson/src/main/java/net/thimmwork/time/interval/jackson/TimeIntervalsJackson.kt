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

package net.thimmwork.time.interval.jackson

import com.fasterxml.jackson.core.Version
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.module.SimpleModule
import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import net.thimmwork.time.interval.InstantInterval
import net.thimmwork.time.interval.LocalDateInterval

/**
 * register this module with a jackson ObjectMapper to enable interval de/serialization
 */
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
