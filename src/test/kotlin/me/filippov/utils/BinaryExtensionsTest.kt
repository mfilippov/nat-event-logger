package me.filippov.utils

import org.junit.Assert
import org.junit.Test

class BinaryExtensionsTest {

    @Test fun shouldCorrectConvertIntToQuadBytes() {
        val result = 1234567890.toBytes()

        Assert.assertEquals(210, result.first.toInt() and 255)
        Assert.assertEquals(2, result.second.toInt() and 255)
        Assert.assertEquals(150, result.third.toInt() and 255)
        Assert.assertEquals(73, result.fourth.toInt() and 255)
    }

    @Test fun shouldCorrectConvertQuadBytesToInt() {
        val result = Quad(210.toByte(), 2.toByte(), 150.toByte(), 73.toByte()).toInt()

        Assert.assertEquals(1234567890, result)
    }

    @Test fun shouldCorrectConvertLongToEightBytes() {
        val result = 1234567890123456789L.toBytes()

        Assert.assertEquals(21, result.first.toInt() and 255)
        Assert.assertEquals(129,  result.second.toInt() and 255)
        Assert.assertEquals(233, result.third.toInt() and 255)
        Assert.assertEquals(125, result.fourth.toInt() and 255)
        Assert.assertEquals(244, result.fifth.toInt() and 255)
        Assert.assertEquals(16, result.sixth.toInt() and 255)
        Assert.assertEquals(34, result.seventh.toInt() and 255)
        Assert.assertEquals(17, result.eighth.toInt() and 255)
    }

    @Test fun shouldCorrectConvertEightBytesToLong() {
        val result = Eight(21.toByte(), 129.toByte(), 233.toByte(), 125.toByte(), 244.toByte(), 16.toByte(), 34.toByte(), 17.toByte()).toLong()

        Assert.assertEquals(1234567890123456789L, result)
    }

    @Test fun shouldCorrectDecode2Byte() {
        val v1 = 80.toBytes()
        Assert.assertEquals(80, Quad(v1.first, v1.second, 0.toByte(), 0.toByte()).toInt())
        val v2 = 65000.toBytes()
        Assert.assertEquals(65000, Quad(v2.first, v2.second, 0.toByte(), 0.toByte()).toInt())
        val v3 = 9000.toBytes()
        Assert.assertEquals(9000, Quad(v3.first, v3.second, 0.toByte(), 0.toByte()).toInt())
    }
}