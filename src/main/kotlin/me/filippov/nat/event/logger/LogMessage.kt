package me.filippov.nat.event.logger

import java.net.InetAddress
import java.time.LocalDateTime

data class LogMessage(
        val time : LocalDateTime,
        val eventType : EventType,
        val src: InetAddress,
        val srcPort: Int,
        val dst: InetAddress,
        val dstPort: Int
)