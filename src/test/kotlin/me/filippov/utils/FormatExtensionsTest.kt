package me.filippov.utils

import org.junit.Assert
import org.junit.Test

class FormatExtensionsTest {

    @Test fun shouldPReturnFormattedString() {
        Assert.assertEquals("010", 10.format("%03d"))
    }

}
