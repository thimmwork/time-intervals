## Time Intervals ##
This library provides data types for intervals of java.time classes.

[![Build Status](https://travis-ci.org/thimmwork/time-intervals.svg?branch=master)](https://travis-ci.org/thimmwork/time-intervals)

### Conventions
LocalDateIntervals are closed, which means that the year 2018 is represented by a LocalDateInterval starting on 2018-01-01 and ending on 2018-12-31.

Instant and DateTimeIntervals are closed-open, which means that the year 2018 is represented by 2018-01-01T:00:00:00 to 2019-01-01T00:00:00 in the respective time zone.

There are constants for Infinity which are
* 1970-01-01 - 4000-12-31 for LocalDate
* 1970-01-01 00:00:00 UTC - 4000-12-31 00:00:00 UTC

respectively. This makes calculations and usage a lot easier, because unbound intervals are treated just like bound ones and fields are always non-null.

To ensure intervals from user input or external sources are within these bounds, use the normalize() function.

### When to use which Java Time data type? ###
* Instant: When describing a point in time, independent of its location, e.g. when did an event happen? what time is it now?
* LocalDate: When describing the date as seen by the user or something beginning or ending that is backed by some contract
* LocalTime: When describing something that (re)occurs at the same local time, independent of daylight saving changes
* LocalDateTime: When describing a date and time as seen by a user
* OffsetDateTime: When describing a point in time and a time-distance from UTC
* ZonedDateTime: When describing a point in time and a location-distance from UTC
* Period: Something that is reoccurring, e.g. every 3 Months, every 2 days
* Duration: An interval of fixed length, e.g. 2 minutes and 23 seconds

### Serialization ###
As of now, serialization via Jackson is supported. Other means may follow in the near future.
Feel free to leave a feature request.

#### Jackson ####
A module that provides JSON de/serialization via Jackson is provided by the artifact
```
    <dependency>
        <groupId>net.thimmwork</groupId>
        <artifactId>time-intervals-jackson</artifactId>
    </dependency>
```

Register it to your ObjectMapper instance like this:
```
    objectMapper.registerModule(TimeIntervalsJackson.MODULE)
```
