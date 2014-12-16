package me.filippov.nat.event.logger

import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import me.filippov.utils.format
import me.filippov.utils.toBytes
import me.filippov.utils.toUnixTime

class LogWriter {
    class object {
        fun writeLog(msg: LogMessage) {
            var array = kotlin.ByteArray(16)
            val src = msg.src.getAddress()
            val srcPort = msg.srcPort.toBytes()
            val dst = msg.dst.getAddress()
            val dstPort = msg.dstPort.toBytes()

            array[0] = msg.time.getHour().toByte()
            array[1] = msg.time.getMinute().toByte()
            array[2] = msg.time.getSecond().toByte()

            array[3] = msg.eventType.value

            array[4] = src[0]
            array[5] = src[1]
            array[6] = src[2]
            array[7] = src[3]
            array[8] = srcPort.first
            array[9] = srcPort.second
            array[10] = dst[0]
            array[11] = dst[1]
            array[12] = dst[2]
            array[13] = dst[3]
            array[14] = dstPort.first
            array[15] = dstPort.second

            Files.write(Paths.get("logs/${msg.time.getYear()}-${msg.time.getMonthValue().format("%02d")}-${msg.time.getDayOfMonth().format("%02d")}.natlog"), array, StandardOpenOption.APPEND, StandardOpenOption.CREATE)
        }
    }
}