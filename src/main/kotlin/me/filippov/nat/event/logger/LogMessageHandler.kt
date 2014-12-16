package me.filippov.nat.event.logger

import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.ChannelHandlerContext

class LogMessageHandler : SimpleChannelInboundHandler<LogMessage>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: LogMessage) {
        LogWriter.writeLog(msg)
    }

}