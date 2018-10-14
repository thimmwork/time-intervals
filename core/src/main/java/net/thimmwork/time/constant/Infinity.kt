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

package net.thimmwork.time.constant

import net.thimmwork.time.interval.InstantInterval
import net.thimmwork.time.interval.LocalDateInterval
import net.thimmwork.time.interval.LocalDateTimeInterval
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * This object contains constants for infinity
 */
object Infinity {
    @JvmStatic val MIN_DATE = LocalDate.of(1970, 1, 1);
    @JvmStatic val MIN_DATE_TIME = MIN_DATE.atStartOfDay();
    @JvmStatic val MAX_DATE = LocalDate.of(4000, 12, 31);
    @JvmStatic val MAX_DATE_TIME = MAX_DATE.atStartOfDay();
    @JvmStatic val LOCAL_DATE_INTERVAL = LocalDateInterval(MIN_DATE, MAX_DATE)
    @JvmStatic val LOCAL_DATE_TIME_INTERVAL = LocalDateTimeInterval(MIN_DATE_TIME, MAX_DATE_TIME)
    @JvmStatic val INSTANT_INTERVAL = InstantInterval(Instant.EPOCH, MAX_DATE.atStartOfDay().toInstant(ZoneOffset.UTC))
}
