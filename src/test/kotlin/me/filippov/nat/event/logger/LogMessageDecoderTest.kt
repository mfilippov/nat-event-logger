package me.filippov.nat.event.logger

import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.embedded.EmbeddedChannel
import io.netty.channel.socket.DatagramPacket
import org.junit.Assert
import org.junit.Test
import java.net.InetSocketAddress
import java.time.LocalDateTime
import java.util.concurrent.LinkedBlockingDeque

class LogMessageDecoderTest {

    @Test fun ShouldDecodeTcpSynLog(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto TCP (SYN), 100.64.0.100:58105->217.107.214.29:80, len 52"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.TCP_SYN, result.eventType)
        Assert.assertEquals("100.64.0.100", result.src.hostAddress)
        Assert.assertEquals(58105, result.srcPort)
        Assert.assertEquals("217.107.214.29", result.dst.hostAddress)
        Assert.assertEquals(80, result.dstPort)
    }

    @Test fun ShouldDecodeUdpLog(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto UDP, 100.64.0.131:34308->178.236.142.83:23224, len 131"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.UDP, result.eventType)
        Assert.assertEquals("100.64.0.131", result.src.hostAddress)
        Assert.assertEquals(34308, result.srcPort)
        Assert.assertEquals("178.236.142.83", result.dst.hostAddress)
        Assert.assertEquals(23224, result.dstPort)
    }

    @Test fun ShouldDecodeUdpLogPrio(){
        val log ="firewall,info srcnat: in:(none) out:Wi-Fi NAT outside, src-mac 00:22:56:b8:00:bf, proto UDP, 10.97.5.10:61916->8.8.8.8:53, prio 1->0, len 72"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.UDP, result.eventType)
        Assert.assertEquals("10.97.5.10", result.src.hostAddress)
        Assert.assertEquals(61916, result.srcPort)
        Assert.assertEquals("8.8.8.8", result.dst.hostAddress)
        Assert.assertEquals(53, result.dstPort)
    }

    @Test fun ShouldDecodeIcmpLog(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto ICMP (type 8, code 0), 100.64.0.213->77.234.40.55, len 60"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.ICMP, result.eventType)
        Assert.assertEquals("100.64.0.213", result.src.hostAddress)
        Assert.assertEquals(0, result.srcPort)
        Assert.assertEquals("77.234.40.55", result.dst.hostAddress)
        Assert.assertEquals(0, result.dstPort)
    }

    @Test fun ShouldDecodeProto41Log(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto 41, 100.64.0.11->93.185.249.146, len 98"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.PROTO41, result.eventType)
        Assert.assertEquals("100.64.0.11", result.src.hostAddress)
        Assert.assertEquals(0, result.srcPort)
        Assert.assertEquals("93.185.249.146", result.dst.hostAddress)
        Assert.assertEquals(0, result.dstPort)
    }

    @Test fun ShouldDecodeTcpAckLog(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto TCP (ACK), 100.64.0.127:51747->217.20.147.94:80, len 41"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.TCP_ACK, result.eventType)
        Assert.assertEquals("100.64.0.127", result.src.hostAddress)
        Assert.assertEquals(51747, result.srcPort)
        Assert.assertEquals("217.20.147.94", result.dst.hostAddress)
        Assert.assertEquals(80, result.dstPort)
    }

    @Test fun ShouldDecodeTcpAckPshLog(){
        val log ="firewall,info srcnat: in:(none) out:sfp-plus2, src-mac 00:25:84:d6:40:3f, proto TCP (ACK,PSH), 100.64.0.165:56566->173.194.32.166:443, len 79"
        val packet = DatagramPacket(Unpooled.wrappedBuffer(log.toByteArray()), InetSocketAddress(514))
        val queue = LinkedBlockingDeque<LogMessage>();
        val channel = EmbeddedChannel(LogMessageDecoder({LocalDateTime.of(2014, 1, 1, 0, 0, 0)}), object : SimpleChannelInboundHandler<LogMessage>() {
            override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage?) {
                queue.add(msg)
            }

        });
        Assert.assertFalse(channel.writeInbound(packet))
        channel.finish()
        Assert.assertEquals(1, queue.size)
        val result = queue.take()
        Assert.assertNotNull(result)
        Assert.assertEquals(LocalDateTime.of(2014, 1, 1, 0, 0, 0), result.time)
        Assert.assertEquals(EventType.TCP_ACK_PSH, result.eventType)
        Assert.assertEquals("100.64.0.165", result.src.hostAddress)
        Assert.assertEquals(56566, result.srcPort)
        Assert.assertEquals("173.194.32.166", result.dst.hostAddress)
        Assert.assertEquals(443, result.dstPort)
    }

}
