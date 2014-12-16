package me.filippov.utils

import java.time.LocalDateTime
import java.time.ZoneOffset

fun LocalDateTime.toUnixTime(): Long {
    return this.toInstant(ZoneOffset.ofHours(0)).toEpochMilli() / 1000
}

fun Long.toLocalDateTime(): LocalDateTime {
    return LocalDateTime.ofEpochSecond(this, 0, ZoneOffset.ofHours(0))
}

