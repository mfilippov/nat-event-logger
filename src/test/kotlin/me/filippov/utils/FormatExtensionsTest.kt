package me.filippov.utils

import org.junit.Test
import kotlin.test.assertEquals

class FormatExtensionsTest {

    Test fun shouldPReturnFormattedString() {
        assertEquals("010", 10.format("%03d"))
    }

}
