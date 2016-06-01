package me.filippov.nat.event.logger

enum class EventType(val value: Byte) {
    TCP_SYN(0),
    UDP(1),
    ICMP(2),
    TCP_ACK(3),
    TCP_ACK_PSH(4),
    PROTO41(5)
}