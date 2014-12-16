package me.filippov.nat.event.logger

import io.netty.channel.ChannelInitializer
import java.time.LocalDateTime
import io.netty.channel.socket.DatagramChannel

class LogChannelInitializer : ChannelInitializer<DatagramChannel>() {

    override fun initChannel(ch: DatagramChannel?) {
        ch?.pipeline()?.addLast(LogMessageDecoder({LocalDateTime.now()}))
        ch?.pipeline()?.addLast(LogMessageHandler())
    }

}