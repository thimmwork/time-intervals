## Time Intervals ##
This library provides data types for intervals of java.time classes.

### Conventions
LocalDateIntervals are closed, which means that the year 2018 is represented by a LocalDateInterval starting on 2018-01-01 and ending on 2018-12-31.

Instant and DateTimeIntervals are closed-open, which means that the year 2018 is represented by 2018-01-01T:00:00:00 to 2019-01-01T00:00:00 in the respective time zone.

There are constants for Infinity which are
* 1970-01-01 - 4000-12-31 for LocalDate
* 1970-01-01 00:00:00 UTC - 4000-12-31 00:00:00 UTC

respectively. This makes calculations and usage a lot easier, because unbound intervals are treated just like bound ones and fields are always non-null.

To ensure intervals from user input or external sources are within these bounds, use the normalize() function.

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
