package me.filippov.nat.event.logger

enum class EventType(val value: Byte) {
    TCP_SYN : EventType(0)
    UDP : EventType(1)
    ICMP : EventType(2)
    TCP_ACK : EventType(3)
    TCP_ACK_PSH : EventType(4)
    PROTO41 : EventType(5)
}