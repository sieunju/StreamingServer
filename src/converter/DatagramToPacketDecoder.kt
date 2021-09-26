package converter

import constants.Header
import io.netty.channel.ChannelHandlerContext
import io.netty.handler.codec.MessageToMessageDecoder
import model.*
import service.ViewerSendManager

/**
 * Description : ByteBuf to BasePacket Decoder
 *
 * Created by juhongmin on 9/17/21
 */
class DatagramToPacketDecoder(
    private val viewerSendManager: ViewerSendManager
) : MessageToMessageDecoder<ReceiveDataPacket>(){
    override fun decode(ctx: ChannelHandlerContext?, msg: ReceiveDataPacket?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return

        when(msg.data[0]) {
            Header.AUTH.type -> {
                val authPacket = AuthPacket.decode(msg.data)
                authPacket.sender = msg.sender
                out.add(authPacket)
            }
            Header.VIDEO.type -> out.add(VideoPacket.decode(msg.data))
            Header.MSG.type -> out.add(MessagePacket.decode(msg.data))
            Header.RELIABLE.type -> {
                // 뷰어로 부터 데이터 패킷을 받은 경우
//                println("Recv Reliable Packet ${msg.data}")
                viewerSendManager.onReliablePacket(msg.sender,ReliablePacket.decode(msg.data))
            }
            else -> {
                
            }
        }
    }
}