package handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import model.MessagePacket
import service.OnReceiveMessage

class ReceiveMessageHandler(private val callback : OnReceiveMessage) : SimpleChannelInboundHandler<MessagePacket>() {

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: MessagePacket?) {
        if(ctx == null || msg == null) return

        callback.onMessage(msg)
    }
}