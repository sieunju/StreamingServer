package handler

import constants.ClientType
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import model.AuthPacket
import service.ViewerSendManager

class ReceiveAuthHandler(
    private val viewerSendManager: ViewerSendManager
) : SimpleChannelInboundHandler<AuthPacket>() {
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: AuthPacket?) {
        if (ctx == null || msg == null) return

        // 보는 사람인 경우에만 사용자 추가.
        if (msg.sender != null && msg.type == ClientType.VIEWER) {
            viewerSendManager.addViewer(msg.sender!!)
        }
    }
}