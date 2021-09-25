package converter

import constants.Constants
import io.netty.buffer.PooledByteBufAllocator
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.socket.DatagramPacket
import io.netty.handler.codec.MessageToMessageDecoder
import model.ReceiveDataPacket
import model.ReliablePacket

class ReliableReceivePacketDecoder : MessageToMessageDecoder<DatagramPacket>() {

    override fun decode(ctx: ChannelHandlerContext?, msg: DatagramPacket?, out: MutableList<Any>?) {
        if (ctx == null || msg == null || out == null) return

        // 좀더 좋은 방법이 없는 고민 해봐야함. 최대한 반복문 안쓰고 ReliableCnt 뺴올수 있는 방법을
        // 구성해야함.
        val bufArray = ByteArray(msg.content().readableBytes())
        msg.content().duplicate().readBytes(bufArray)
        val message = String(bufArray)

        // Reliable Packet 전송.
        val reliablePacket = ReliablePacket(message.split(Constants.SEP)[1])
        val reliableBuf = PooledByteBufAllocator.DEFAULT.directBuffer()
        reliableBuf.writeBytes(reliablePacket.encode().toByteArray())
        ctx.writeAndFlush(DatagramPacket(reliableBuf, msg.sender()))
        println("Reliable Packet ${reliablePacket.uid}")

        out.add(
            ReceiveDataPacket(
                sender = msg.sender(),
                data = message
            )
        )
    }
}