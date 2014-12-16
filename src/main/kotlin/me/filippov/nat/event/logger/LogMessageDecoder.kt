package me.filippov.nat.event.logger

import io.netty.handler.codec.MessageToMessageDecoder
import io.netty.channel.socket.DatagramPacket
import java.util.regex.Pattern
import io.netty.channel.ChannelHandlerContext
import java.nio.charset.Charset
import java.net.InetAddress
import java.time.LocalDateTime

class LogMessageDecoder(val timestampProvider: () -> LocalDateTime) : MessageToMessageDecoder<DatagramPacket>() {
    private val tcpSynPattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\sTCP\\s\\(SYN\\),\\s(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),\\slen\\s\\d+$")
    private val tcpAckPattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\sTCP\\s\\(ACK\\),\\s(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),\\slen\\s\\d+$")
    private val tcpAckPshPattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\sTCP\\s\\(ACK,PSH\\),\\s(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),\\slen\\s\\d+$")
    private val udpPattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\sUDP,\\s(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+):(\\d+),\\slen\\s\\d+$")
    private val icmpPattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\sICMP\\s\\(type\\s8,\\scode\\s0\\),\\s(\\d+\\.\\d+\\.\\d+\\.\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+),\\slen\\s\\d+$")
    private val proto41Pattern = Pattern.compile("^firewall,info\\ssrcnat:\\sin:\\(none\\)\\sout:sfp-plus2,\\ssrc-mac\\s\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2}:\\S{2},\\sproto\\s41,\\s(\\d+\\.\\d+\\.\\d+\\.\\d+)->(\\d+\\.\\d+\\.\\d+\\.\\d+),\\slen\\s\\d+$")

    public override fun decode(ctx: ChannelHandlerContext?, msg: DatagramPacket?, out: MutableList<Any>?) {
        val log = msg?.content()?.toString(Charset.forName("UTF-8"))

        if (log != null) {
            var m = tcpSynPattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.TCP_SYN,
                        InetAddress.getByName(m.group(1)),
                        m.group(2).toInt(),
                        InetAddress.getByName(m.group(3)),
                        m.group(4).toInt()
                    )
                )
                return
            }
            m = tcpAckPattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.TCP_ACK,
                        InetAddress.getByName(m.group(1)),
                        m.group(2).toInt(),
                        InetAddress.getByName(m.group(3)),
                        m.group(4).toInt()
                    )
                )
                return
            }
            m = tcpAckPshPattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.TCP_ACK_PSH,
                        InetAddress.getByName(m.group(1)),
                        m.group(2).toInt(),
                        InetAddress.getByName(m.group(3)),
                        m.group(4).toInt()
                    )
                )
                return
            }
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.TCP_SYN,
                        InetAddress.getByName(m.group(1)),
                        m.group(2).toInt(),
                        InetAddress.getByName(m.group(3)),
                        m.group(4).toInt()
                    )
                )
                return
            }
            m = udpPattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.UDP,
                        InetAddress.getByName(m.group(1)),
                        m.group(2).toInt(),
                        InetAddress.getByName(m.group(3)),
                        m.group(4).toInt()
                    )
                )
                return
            }
            m = icmpPattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.ICMP,
                        InetAddress.getByName(m.group(1)),
                        0,
                        InetAddress.getByName(m.group(2)),
                        0
                    )
                )
                return
            }
            m = proto41Pattern.matcher(log)
            if (m.matches()) {
                out?.add(
                    LogMessage(
                        timestampProvider(),
                        EventType.PROTO41,
                        InetAddress.getByName(m.group(1)),
                        0,
                        InetAddress.getByName(m.group(2)),
                        0))
                return
            }
            println("Unknown format: ${log}")
        } else {
            println("Message is null")
        }
    }
}