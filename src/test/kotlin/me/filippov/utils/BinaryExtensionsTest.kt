package me.filippov.utils

import org.junit.Test
import kotlin.test.assertEquals

class BinaryExtensionsTest {

    Test fun shouldCorrectConvertIntToQuadBytes() {
        val result = 1234567890.toBytes()

        assertEquals(210, result.first.toInt() and 255)
        assertEquals(2, result.second.toInt() and 255)
        assertEquals(150, result.third.toInt() and 255)
        assertEquals(73, result.fourth.toInt() and 255)
    }

    Test fun shouldCorrectConvertQuadBytesToInt() {
        val result = Quad(210.toByte(), 2.toByte(), 150.toByte(), 73.toByte()).toInt()

        assertEquals(1234567890, result)
    }

    Test fun shouldCorrectConvertLongToEightBytes() {
        val result = 1234567890123456789L.toBytes()

        assertEquals(21, result.first.toInt() and 255)
        assertEquals(129,  result.second.toInt() and 255)
        assertEquals(233, result.third.toInt() and 255)
        assertEquals(125, result.fourth.toInt() and 255)
        assertEquals(244, result.fifth.toInt() and 255)
        assertEquals(16, result.sixth.toInt() and 255)
        assertEquals(34, result.seventh.toInt() and 255)
        assertEquals(17, result.eighth.toInt() and 255)
    }

    Test fun shouldCorrectConvertEightBytesToLong() {
        val result = Eight(21.toByte(), 129.toByte(), 233.toByte(), 125.toByte(), 244.toByte(), 16.toByte(), 34.toByte(), 17.toByte()).toLong()

        assertEquals(1234567890123456789L, result)
    }

    Test fun shouldCorrectDecode2Byte() {
        var v1 = 80.toBytes()
        assertEquals(80, Quad(v1.first, v1.second, 0.toByte(), 0.toByte()).toInt())
        var v2 = 65000.toBytes()
        assertEquals(65000, Quad(v2.first, v2.second, 0.toByte(), 0.toByte()).toInt())
        var v3 = 9000.toBytes()
        assertEquals(9000, Quad(v3.first, v3.second, 0.toByte(), 0.toByte()).toInt())
    }
}