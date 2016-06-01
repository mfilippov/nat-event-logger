package me.filippov.utils

import org.junit.Assert
import org.junit.Test
import java.time.LocalDateTime

class UnixTimeTest {

    @Test fun shouldCorrectConvertLocalDateTimeToUnixTime() {
        Assert.assertEquals(981173106L, LocalDateTime.of(2001, 2, 3, 4, 5, 6).toUnixTime())
    }

    @Test fun shouldCorrectConvertUnixTimeToLocalDateTime() {
        Assert.assertEquals(LocalDateTime.of(2001, 2, 3, 4, 5, 6), 981173106L.toLocalDateTime())
    }
}