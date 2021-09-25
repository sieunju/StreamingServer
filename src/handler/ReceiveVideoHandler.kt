package handler

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import model.VideoPacket
import service.OnReceiveMessage

class ReceiveVideoHandler (private val callback : OnReceiveMessage) : SimpleChannelInboundHandler<VideoPacket> (){

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: VideoPacket?) {
        if(ctx == null || msg == null) return
        callback.onVideo(msg)
    }
}